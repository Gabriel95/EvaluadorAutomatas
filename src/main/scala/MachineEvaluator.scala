import scala.collection.mutable.ListBuffer

/**
  * Created by Gabriel Paz on 2/1/2016.
  */
class MachineEvaluator {

  def getInitialNode(nodeList : ListBuffer[CustomNode]): CustomNode =
  {
    for(it <- nodeList) {
      if(it.isInitial)
        return it
    }
      null
  }

  def getNextNode(origin: CustomNode, transition: String, edgeList : ListBuffer[CustomEdge]): CustomNode =
  {
    for(it <- edgeList) {
      if(it.Origin.equals(origin)&&it.Transition.equals(transition))
      {
        return it.Destiny
      }
    }
    null
  }

  def evaluateFDA(str : String, nodeList : ListBuffer[CustomNode],edgeList : ListBuffer[CustomEdge]) : Boolean = {
    var currentNode = getInitialNode(nodeList)
    for(c <- str){
      currentNode = getNextNode(currentNode,c.toString,edgeList)
      if (currentNode == null){
        return false
      }
    }
    return currentNode.accepted
  }

}
