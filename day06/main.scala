import scala.collection.mutable.Stack

object Day06 {
  def nodeConnections(graph: Map[String, Array[String]], node: String): Int = {
    if (!graph.contains(node))
      return 0

    graph(node).size + graph(node).map(nodeConnections(graph, _)).sum
  }

  def minSteps(graph: Map[String, Array[String]], origin: String, destination: String): Int = {
    var dist = collection.mutable.Map(graph.map { case (k, v) => k -> 0 }.toSeq: _*)
    var prev = collection.mutable.Map(graph.map { case (k, v) => k -> "" }.toSeq: _*)
    var done = false

    var q = Stack(origin)
    while (!q.isEmpty && !done) {
      val node = q.pop()

      if (node == destination) {
        done = true
      } else {
        val neighbors = nodeNeighbors(graph, node)
        for (neighbor <- neighbors) {
          if (dist(neighbor) == 0) {
            dist(neighbor) = 1
            prev(neighbor) = node
            q.push(neighbor)
          }
        }
      }
    }

    var path = Array[String](destination)
    while (path.last != origin) {
      path = path :+ prev(path.last)
    }

    path.size - 3
  }

  def nodeNeighbors(graph: Map[String, Array[String]], node: String): Array[String] = {
    graph(node) ++ graph.keys.filter(n => graph(n).contains(node))
  }

  def main(args: Array[String]): Unit = {
    val lines = scala.io.Source.fromFile("input").mkString

    val edges = lines.split("\n")
      .toArray
      .map(_.trim)
      .filter(_ != "")
      .map(_.split("\\)"))
      .map { case Array(f1,f2) => (f1,f2) }

    val graph: Map[String, Array[String]] = edges.groupBy(_._2)
      .map { case (k, v) => k -> v.map(_._1) }

    val connections = graph.map { case (k, v) => k -> nodeConnections(graph, k) }
    val answer1 = connections.values.sum

    val answer2 = minSteps(graph, "YOU", "SAN")

    println(answer1)
    println(answer2)
  }
}
