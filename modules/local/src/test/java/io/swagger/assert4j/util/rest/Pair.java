package io.swagger.assert4j.util.rest;

/**
 * A Tuple pair for usage with values that a tightly coupled together
 *
 * @param <L> The left part of the tuple
 * @param <R> The right part of the tuple
 */
public class Pair<L, R> {

    private final L left;
    private final R right;

    /**
     * Creates a tightly coupled pair with a left and a right part
     *
     * @param left  The left part of the pair
     * @param right The rightpart of the pair
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.getLeft()) &&
                this.right.equals(pairo.getRight());
    }
}