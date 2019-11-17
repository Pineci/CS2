package edu.caltech.cs2.project08.bots;

import edu.caltech.cs2.project08.game.Board;
import edu.caltech.cs2.project08.game.Evaluator;
import edu.caltech.cs2.project08.game.Move;
import edu.caltech.cs2.project08.game.SimpleEvaluator;
import edu.caltech.cs2.project08.interfaces.IDeque;

public class MinimaxSearcher<B extends Board> extends AbstractSearcher<B> {
    @Override
    public Move getBestMove(B board, int myTime, int opTime) {
        BestMove best = minimax(this.evaluator, board, ply);
        return best.move;
    }

    private static <B extends Board> BestMove minimax(Evaluator<B> evaluator, B board, int depth) {
        if(depth == 0){
            return new BestMove(evaluator.eval(board));
        }

        IDeque<Move> moves = board.getMoves();
        BestMove curr = null;
        for(Move m : moves){
            board.makeMove(m);
            BestMove b = minimax(evaluator, board, depth-1);
            b.score *= -1;
            b.move = m;
            board.undoMove();
            if(curr == null || curr.score < b.score){
                curr = b;
            }
        }

        return curr;

    }
}
