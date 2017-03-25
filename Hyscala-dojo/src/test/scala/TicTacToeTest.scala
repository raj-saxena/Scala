import org.scalatest.{FunSuite, Matchers}

class TicTacToeTest extends FunSuite with Matchers {
  val playerOne = Player("X")
  val playerTwo = Player("0")
  val board = new Board(playerOne, playerTwo)

  test("should be able to create the board with empty positions and have 2 players alternating their moves") {
    val locations = board.positions

    assert(locations.toSet == Set(
      Position(0, 0), Position(0, 1), Position(0, 2),
      Position(1, 0), Position(1, 1), Position(1, 2),
      Position(2, 0), Position(2, 1), Position(2, 2)))

    assert(board.players == List(playerOne, playerTwo))
    assert(board.currentPlayer == playerOne)
  }

  test("should be able to mark a position and update currentPlayer after move") {
    val input = "0 1"
    val x :: y :: Nil = input.split(" ").toList.map(_.toInt)

    // Should be available
    assert(board.checkAvailable(Position(x, y)))
    assert(board.currentPlayer == playerOne)

    // update the location with player symbol
    board.updatePosition(Position(x, y, playerOne.symbol))

    assert(!board.checkAvailable(Position(x, y)))
    assert(board.currentPlayer == playerTwo)
  }

  test("should be able to declare winner if 3 moves in a row") {
    val playerOne = Player("X")
    val playerTwo = Player("0")
    val board = new Board(playerOne, playerTwo)
    board.updatePosition(Position(0, 0, playerOne.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(1, 0, playerTwo.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(0, 1, playerOne.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(1, 1, playerTwo.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(0, 2, playerOne.symbol))
    assert(board.winner.contains(playerOne))
  }

  test("should be able to declare winner if 3 moves in a column") {
    val playerOne = Player("X")
    val playerTwo = Player("0")
    val board = new Board(playerOne, playerTwo)
    board.updatePosition(Position(0, 0, playerOne.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(0, 1, playerTwo.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(1, 0, playerOne.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(1, 1, playerTwo.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(2, 0, playerOne.symbol))
    assert(board.winner.contains(playerOne))
  }

  test("should be able to declare winner if 3 moves diagonally") {
    val playerOne = Player("X")
    val playerTwo = Player("0")
    val board = new Board(playerOne, playerTwo)
    board.updatePosition(Position(0, 0, playerOne.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(0, 1, playerTwo.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(1, 1, playerOne.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(1, 0, playerTwo.symbol))
    assert(board.winner.isEmpty)
    board.updatePosition(Position(2, 2, playerOne.symbol))
    assert(board.winner.contains(playerOne))
  }
}
