/**
  * Created by Gabriel Paz on 1/30/2016.
  */

import javafx.collections.ObservableList
import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.{Node, Scene}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.shape.{QuadCurve, Line, Rectangle}
import javafx.scene.input.MouseEvent
import javafx.event.{ActionEvent, EventHandler}
import scalafx.scene.control.{Alert, ComboBox, Button, TextInputDialog}

object main extends JFXApp {
  var nodeList = ListBuffer.empty[CustomNode]
  var edgeList = ListBuffer.empty[CustomEdge]
  var comboBox = new ComboBox(List("")){
    layoutX = 810
    layoutY = 50
  }
  val evaluator = new MachineEvaluator()
  var comboBox2 = new ComboBox(List("")){
    layoutX = 810
    layoutY = 100
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Scala Sucks"
    width = 1000
    height = 800
    scene = new Scene {
      fill = LightGray



      onMouseClicked = new EventHandler[MouseEvent] {
        override def handle(t: MouseEvent): Unit =
        {
          var collides = false
          for(it <- nodeList)
          {
            val valueX = it.circle.centerX.value - t.getX.getValue
            val valueY = it.circle.centerY.value - t.getY.getValue
            val intersect = (valueX*valueX) + (valueY*valueY)
            val squareRadius = (it.circle.radius.value+it.circle.radius.value)*(it.circle.radius.value+it.circle.radius.value)
            if(0 <= intersect && intersect <= squareRadius)
              collides = true
          }

          if(!collides && t.getX <= 720){
            val dialog = new TextInputDialog() {
              initOwner(stage)
              title = "New Node Name"
              headerText = "Input new node's name."
            }
            var mot = ""
            val result = dialog.showAndWait()
            result match {
              case Some(name) => mot = name
                val nodeToAdd = addNewNode(mot,t.getX, t.getY)
                content.add(nodeToAdd.circle)
                content.add(nodeToAdd.text)
                nodeList += nodeToAdd
                comboBox.+=(nodeToAdd.name)
                comboBox2.+=(nodeToAdd.name)
              case None       => println("Dialog was canceled.")
            }

          }
        }
      }

      var sidebarBox = new Rectangle()
      {
        x = 750
        y = 0
        height = 800
        width = 350
        fill = Color.Gray
      }

      val deleteButton = new Button("Delete"){
        layoutX = 780
        layoutY = 150
        onAction = new EventHandler[ActionEvent] {
          override def handle(event: ActionEvent): Unit = {
            content.removeAll()
            deleteNode(comboBox.getValue)
            for(it <- nodeList)
            {
              content.add(it.circle)
              content.add(it.text)
            }
            for(it <- edgeList){
              content.add(it.line)
              content.add(it.Marker)
              it.line.toBack()
            }
          }
        }
      }

      val addEdgeButton = new Button("Add Edge"){
        layoutX = 850
        layoutY = 150
        onAction = new EventHandler[ActionEvent] {
          override def handle(event: ActionEvent): Unit = {

            val dialog = new TextInputDialog() {
              initOwner(stage)
              title = "Edge Value"
              headerText = "Input new edge's value"
            }
            var mot = ""
            val result = dialog.showAndWait()

            result match {
              case Some(name) => mot = name
                val edgeToAdd = addNewEdge(comboBox.getValue, comboBox2.getValue, name)
                edgeList += edgeToAdd
                content.add(edgeToAdd.text)
                if(!edgeToAdd.Destiny.equals(edgeToAdd.Origin))
                {
                  content.add(edgeToAdd.line)
                  content.add(edgeToAdd.Marker)
                  edgeToAdd.line.toBack()
                }else{
                  content.add(edgeToAdd.curvedLine)
                  edgeToAdd.curvedLine.toBack()
                }

              case None       => println("Dialog was canceled.")
            }
          }
        }
      }

      val markAcceptedButton = new Button("Un/Mark as Accepted"){
        layoutX = 780
        layoutY = 200
        onAction = new EventHandler[ActionEvent] {
          override def handle(event: ActionEvent): Unit = {
            val tempNode = retrieveNode(comboBox.getValue)
            if(tempNode.accepted){
              if(tempNode.isInitial)
                tempNode.circle.fill = Color.PaleGreen
              else
                tempNode.circle.fill = Color.White
              tempNode.accepted = false
            }else{
              tempNode.circle.fill = Color.LightBlue
              tempNode.accepted = true
            }
          }
        }
      }

      val evaluateButton = new Button("Evaluate a String"){
        layoutX = 780
        layoutY = 250
        onAction = new EventHandler[ActionEvent] {
          override def handle(event: ActionEvent): Unit = {
            val dialog = new TextInputDialog() {
              initOwner(stage)
              title = "Input String"
              headerText = "Input string to be evaluated"
            }
            var str = ""
            val result = dialog.showAndWait()
            result match {
              case Some(name) => str = name
                if(evaluator.evaluateFDA(str,nodeList,edgeList)){
                  new Alert(AlertType.Information) {
                    initOwner(stage)
                    title = "Succes"
                    headerText = "String Accepted"
                    contentText = "The string is accepted by the current machine"
                  }.showAndWait()
                }else{
                  new Alert(AlertType.Error) {
                    initOwner(stage)
                    title = "Error"
                    headerText = "String not accepted."
                    contentText = "The string is not accepted by the current machine."
                  }.showAndWait()
                }
              case None       => println("Dialog was canceled.")
            }
          }
        }
      }

      comboBox.-=("")
      comboBox2.-=("")
      content.add(sidebarBox)
      content.add(comboBox)
      content.add(comboBox2)
      content.add(deleteButton)
      content.add(addEdgeButton)
      content.add(markAcceptedButton)
      content.add(evaluateButton)
    }

  }

  def retrieveNode(name:String) : CustomNode = {
    for(it <- nodeList){
      if(it.name.equals(name)){
         return it
      }
    }
    null
  }

  def addNewNode(name:String, x:Double, y:Double) : CustomNode = {
    val nodeToReturn = new CustomNode()
    nodeToReturn.setDimensions(name,x,y)
    if(nodeList.isEmpty){
      nodeToReturn.isInitial = true
      nodeToReturn.circle.fill = Color.PaleGreen
      nodeToReturn.circle.stroke = Color.PaleGreen
    }
    nodeToReturn
  }

  def addNewEdge(origin:String, destiny:String, value: String) : CustomEdge = {
    val edgeToReturn = new CustomEdge()
    edgeToReturn.Init(retrieveNode(origin),retrieveNode(destiny),value)
    edgeToReturn
  }

  def deleteNode(nameNodeToDelete:String) : Unit = {
    for(it <- nodeList){
      if(it.name.equals(nameNodeToDelete)){
        nodeList.remove(nodeList.indexOf(it))
        comboBox.-=(it.name)
        comboBox2.-=(it.name)
      }
    }

    for(it <- edgeList){
      if(it.Origin.name.equals(nameNodeToDelete) || it.Destiny.name.equals(nameNodeToDelete)){
        edgeList.remove(edgeList.indexOf(it))
      }
    }
  }

}