package typed

import actors.Person.Greet
import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

sealed trait PersonProtocol
case class Greet(person: ActorRef[PersonProtocol]) extends PersonProtocol
case class Hello(sender: ActorRef[PersonProtocol]) extends PersonProtocol
case class WhatsYourName(sender: ActorRef[PersonProtocol]) extends PersonProtocol
case class NameReply(sender: ActorRef[PersonProtocol], name: String) extends PersonProtocol

object Person {
  def apply(name: String): Behavior[PersonProtocol] = start(name)

  private def start(name: String): Behavior[PersonProtocol] = Behaviors.receive { (context, message) =>
    message match {
      case Greet(person) =>
        person ! Hello(context.self)

        sentGreetings(name, person)
      case Hello(sender) =>
        println(s"$name was greeted")

        sender ! Hello(context.self)

        greeted(name, sender)
    }
  }

  private def sentGreetings(
    name: String,
    to: ActorRef[PersonProtocol]
  ): Behavior[PersonProtocol] = Behaviors.receive { (context, message) =>
    message match {
      case Hello(sender) =>
        println(s"$name was greeted")

        sender ! WhatsYourName(context.self)

        Behaviors.same

      case NameReply(sender, otherPersonName) =>
        if (sender == to)
          println(s"$name received other person name: $otherPersonName")
        else
          println(s"A stranger introduced themselves to me ($name)")

        start(name)
    }
  }

  def greeted(
     name: String,
     by: ActorRef[PersonProtocol]
 ): Behavior[PersonProtocol] = Behaviors.receive { (context, message) =>
    message match {
      case WhatsYourName(sender) =>
        if (sender == by) {
          println(s"$name was asked for name")

          sender ! NameReply(context.self, name)
        } else println(s"$name asked for name by a stranger")

        start(name)
    }
  }
}

object ActorsExample extends App {
  def apply(): Behavior[NotUsed] = Behaviors.setup { context =>
    val george = context.spawn(Person("George"), "george")
    val john = context.spawn(Person("John"), "john")

    george ! Greet(john)

    Behaviors.same
  }

  ActorSystem(ActorsExample(), "ActorsExample3Typed")
}
