class Board(playerOne: Player, playerTwo: Player) {
  var winner: Option[Player] = None

  def checkAvailable(position: Position) = getPosition(position).symbol == null

  def checkWinner(currentPosition: Position) = {
    val winningCondition = (pos:Position) => pos.symbol == currentPosition.symbol
    if (positions.filter(_.x == currentPosition.x).forall(winningCondition) ||
      positions.filter(_.y == currentPosition.y).forall(winningCondition) ||
      positions.filter(p => p.x == p.y).forall(winningCondition))
      winner = Some(currentPlayer)
  }

  def updatePosition(position: Position) = {
    val positionToUpdate = getPosition(position)
    positionToUpdate.symbol = position.symbol

    checkWinner(position)

    currentPlayer = players.filterNot(_ == currentPlayer).head
  }

  private def getPosition(p: Position) = {
    positions.find(pos => pos.x == p.x && pos.y == p.y).get
  }

  var players: List[Player] = List(playerOne, playerTwo)

  var currentPlayer = players.head


  var positions = (for {i <- 0 to 2
                        j <- 0 to 2} yield Position(i, j)).toList
}
