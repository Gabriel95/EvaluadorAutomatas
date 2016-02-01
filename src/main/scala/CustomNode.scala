/**
  * Created by Gabriel Paz on 1/30/2016.
  */
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.text._
class CustomNode {
  var circle: Circle = null
  var text: Text = null
  var name: String = ""
  var accepted = false
  var isInitial = false
  var active = true
  def setDimensions(name:String, x:Double, y:Double): Unit ={
    circle = new Circle() {
      radius = 30
      centerX = x
      centerY = y
      fill = Color.White
      stroke = Color.Black
    }

    text = new Text(x-20,y+4,name)
    this.name = name
  }
}
