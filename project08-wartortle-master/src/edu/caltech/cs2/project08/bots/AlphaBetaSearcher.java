package edu.caltech.cs2.project08.bots;

import edu.caltech.cs2.project08.game.Board;
import edu.caltech.cs2.project08.game.Evaluator;
import edu.caltech.cs2.project08.game.Move;
import edu.caltech.cs2.project08.interfaces.IDeque;

public class AlphaBetaSearcher<B extends Board> extends AbstractSearcher<B> {
    @Override
    public Move getBestMove(B board, int myTime, int opTime) {
        BestMove best = alphaBeta(this.evaluator, board, ply);
        return best.move;
    }

    private static <B extends Board> BestMove alphaBeta(Evaluator<B> evaluator, B board, int depth) {
        return alphaBeta(evaluator, board, depth, -1000, 1000);
    }

    private static <B extends Board> BestMove alphaBeta(Evaluator<B> evaluator, B board, int depth, int alpha, int beta){
        if(depth == 0){
            return new BestMove(evaluator.eval(board));
        }

        IDeque<Move> moves = board.getMoves();
        BestMove curr = null;

        for(Move m : moves){
            board.makeMove(m);
            BestMove b = alphaBeta(evaluator, board, depth-1, -1 * beta, -1 * alpha);
            b.score *= -1;
            b.move = m;
            board.undoMove();

            if(b.score > alpha){
                alpha = b.score;
            }

            if(alpha >= beta){
                b.score = alpha;
                return b;
            }
            if(curr == null || curr.score < b.score){
                curr = b;
            }
        }

        return curr;

    }
}