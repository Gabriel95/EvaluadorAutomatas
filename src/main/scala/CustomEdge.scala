import scalafx.scene.paint.Color
import scalafx.scene.shape.{QuadCurve, Circle, Line}
import scalafx.scene.text.Text

/**
  * Created by Gabriel Paz on 1/30/2016.
  */
class CustomEdge {
  var Origin : CustomNode = null
  var Destiny : CustomNode = null
  var Marker : Circle = null
  var Transition : String = ""
  var line : Line = null
  var text: Text = null
  var curvedLine: QuadCurve = null

  def Init( origin: CustomNode, destiny : CustomNode, transition : String) : Unit = {
    Origin = origin
    Destiny = destiny
    Transition = transition

    if(!origin.equals(destiny)){
      val ox = Origin.circle.centerX.value
      var oy = Origin.circle.centerY.value
      val dx = Destiny.circle.centerX.value
      var dy = Destiny.circle.centerY.value

      text = new Text((ox+dx)/2,(oy+dy)/2,Transition)

      line = Line(ox,oy,dx,dy)
      oy = 800 - oy
      dy = 800 - dy
      val m = (dy - oy) / (dx - ox)
      var mx = 0.0
      var my = dy

      if(ox < dx){
        mx = dx -30
      }else if (ox > dx){
        mx = dx + 30
      }else{
        mx = dx
        my = my + 30
      }
      val formula =  m*(mx - dx) + my

      Marker = new Circle(){
        radius = 4
        centerX = mx
        centerY = 800-formula
        fill = Color.LightGray
        stroke = Color.Black
      }
    }else{
      curvedLine =  QuadCurve(Destiny.circle.centerX.value-20,Destiny.circle.centerY.value,
        Destiny.circle.centerX.value,Destiny.circle.centerY.value - 110,
        Destiny.circle.centerX.value+20, Destiny.circle.centerY.value)
      curvedLine.fill = Color.LightGray
      curvedLine.stroke = Color.Black

      text = new Text(Destiny.circle.centerX.value,Destiny.circle.centerY.value - 60,Transition)
    }

  }
}
