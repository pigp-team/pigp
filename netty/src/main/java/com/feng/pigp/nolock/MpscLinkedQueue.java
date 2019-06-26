package com.feng.pigp.nolock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author feng
 * @date 2019/6/12 18:49
 * @since 1.0
 */
public class MpscLinkedQueue <E>{

    long p00, p01, p02, p03, p04, p05, p06, p07;
    long p30, p31, p32, p33, p34, p35, p36, p37;

    private transient volatile Node<E> tailRef;
    private transient  volatile Node<E> headRef;

    private static final AtomicReferenceFieldUpdater<MpscLinkedQueue, Node> TAIL_UPDATER = AtomicReferenceFieldUpdater.newUpdater(
            MpscLinkedQueue.class, Node.class, "tailRef");
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueue, Node> HEAD_UPDATER = AtomicReferenceFieldUpdater.newUpdater(
            MpscLinkedQueue.class, Node.class, "headRef");

    MpscLinkedQueue() {
        Node<E> tombstone = new Node<E>();
        setHeadRef(tombstone);
        setTailRef(tombstone);
    }

    private Node<E> peekNode() {
        Node<E> head = headRef;
        Node<E> next = head.getNext();
        if (next == null && head != tailRef) {
            do {
                next = head.getNext();
            } while (next == null);
        }
        return next;
    }

    public boolean offer(E value) {
        if (value == null) {
            throw new NullPointerException("value");
        }

        final Node<E> newTail = new Node<>();
        newTail.setValue(value);

        Node<E> oldTail = getAndSetTailRef(newTail);
        oldTail.setNext(newTail);
        return true;
    }

    protected final Node<E> getAndSetTailRef(Node<E> tailRef) {
        return (Node<E>) TAIL_UPDATER.getAndSet(this, tailRef);
    }

    public E poll() {
        final Node<E> next = peekNode();
        if (next == null) {
            return null;
        }

        Node<E> oldHead = headRef;
        HEAD_UPDATER.lazySet(this, next);

        oldHead.setNext(null);
        return next.getValue();
    }

    public void setHeadRef(Node<E> headRef) {
        HEAD_UPDATER.getAndSet(this, headRef);
    }

    public void setTailRef(Node<E> tailRef) {
        TAIL_UPDATER.getAndSet(this, tailRef);
    }

    public static class Node<E>{

        E value;
        Node next;

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}