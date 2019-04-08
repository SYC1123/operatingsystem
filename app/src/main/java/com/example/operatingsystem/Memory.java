package com.example.operatingsystem;

import android.widget.TextView;

public class Memory {
    private int begin;// 起始地址
    private int end;// 结束地址
    private int size;// 大小
    private Memory next;
    private Memory before;

    public Memory(int begin, int size) {
        // TODO 自动生成的构造函数存根
        this.begin = begin;
        this.size = size;
        this.end = begin + size;
    }

    public int addPCB(PCB pcb,TextView textView) {
        Memory t = this;
        t.bubbleSortForsize();
        t = this.next;
        if (t.next == null) {
            if (t.size == 0 || t.size < pcb.getSize()) {
                textView.setText("无可用内存！");
                return -1;
            } else {
                pcb.setBegin(t.begin);
                pcb.setEnd(pcb.getBegin() + pcb.getSize());
                t.begin = t.begin + pcb.getSize();
                t.size -= pcb.getSize();
                return 1;
            }
        } else {
            while (t.hasnext()) {
                if (pcb.getSize() <= t.getSize()) {
                    if (t.getSize() - pcb.getSize() <= 2) {
                        pcb.setBegin(t.begin);
                        pcb.setEnd(t.end);
                        t.before.next = t.next;
                        t.next.before = t.before;
                        break;
                    } else {
                        pcb.setBegin(t.begin);
                        pcb.setEnd(t.begin + pcb.getSize());
                        t.begin = pcb.getEnd();
                        t.size = t.getSize() - pcb.getSize();
                        break;
                    }
                } else {
                    // 可添加紧凑操作
                    textView.setText("无可用内存！");
                    // break;
                }
                t = t.next;
            }
            return 1;
        }
    }

    public void endPCB(PCB pcb) {
        Memory t = this;
        t.bubbleSortForAddress();
        t = this.next;
        Memory memory = new Memory(pcb.getBegin(), pcb.getSize());
        if (t.next == null) {
            if (pcb.getBegin() < t.begin) {
                if (pcb.getEnd() == t.begin) {
                    t.begin = pcb.getBegin();
                    t.size += pcb.getSize();
                } else {
                    this.next = memory;
                    memory.before = this;
                    memory.next=null;
                }
            } else {
                if (memory.begin == t.end) {
                    t.end = memory.end;
                    t.size += memory.size;
                } else {
                    t.next = memory;
                    memory.before = t;
                    memory.next = null;
                }
            }

        } else {
            if (pcb.getBegin() < t.begin) {
                if (pcb.getEnd() == t.begin) {
                    t.begin = pcb.getBegin();
                    t.size += pcb.getSize();
                } else {
                    this.next = memory;
                    memory.before = this;
                    t.before = memory;
                    memory.next = t;
                }
            } else {
                while (t.hasnext()) {
                    if (t.next.next != null) {
                        if (pcb.getBegin() > t.begin && pcb.getBegin() < t.next.begin) {
                            if (memory.begin == t.end && memory.end == t.next.begin) {
                                t.end = t.next.end;
                                t.size = t.size + memory.size + t.next.size;
                                t.next.next.before = t;
                                t.next = t.next.next;
                            } else if (memory.begin == t.end && memory.end != t.next.begin) {
                                t.end = memory.end;
                                t.size += memory.size;
                            } else if (memory.begin != t.end && memory.end == t.next.begin) {
                                t.next.begin = memory.begin;
                                t.next.size += memory.size;
                            } else {
                                t.next.before = memory;
                                memory.next = t.next;
                                t.next = memory;
                                memory.before = t;
                            }
                            break;
                        }
                    } else {
                        if (pcb.getBegin() > t.begin && pcb.getBegin() < t.next.begin) {
                            if (memory.begin == t.end && memory.end == t.next.begin) {
                                t.end = t.next.end;
                                t.size = t.size + memory.size + t.next.size;
                                t.next = null;
                            } else if (memory.begin == t.end && memory.end != t.next.begin) {
                                t.end = memory.end;
                                t.size += memory.size;
                            } else if (memory.begin != t.end && memory.end == t.next.begin) {
                                t.next.begin = memory.begin;
                                t.next.size += memory.size;
                            } else {
                                t.next.before = memory;
                                memory.next = t.next;
                                t.next = memory;
                                memory.before = t;
                            }
                            break;
                        } else {
                            if (memory.begin == t.next.end) {
                                t.next.end = memory.end;
                                t.next.size += memory.size;
                            } else {
                                t.next.next = memory;
                                memory.before = t.next;
                            }
                            break;
                        }
                    }
                    t = t.next;
                }
            }
        }

    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Memory getNext() {
        return next;
    }

    public void setNext(Memory next) {
        this.next = next;
    }

    public Memory getBefore() {
        return before;
    }

    public void setBefore(Memory before) {
        this.before = before;
    }

    public Boolean hasnext() {
        return this.next == null ? false : true;
    }

    // 容量大小冒泡排序
    public void bubbleSortForsize() {
        if (this == null || this.next == null) // 链表为空或者仅有单个结点
        {
        } else {
            Memory cur = null, tail = null;
            cur = this.next;
            while (cur.next != tail) {
                while (cur.next != tail) {
                    if (cur.size < cur.next.size) {
                        int tmp_size = cur.size;
                        int tmp_begin = cur.begin;
                        cur.size = cur.next.size;
                        cur.begin = cur.next.begin;
                        cur.next.size = tmp_size;
                        cur.next.begin = tmp_begin;
                    }
                    cur = cur.next;
                }
                tail = cur;
                cur = this.next;
            }
        }

    }

    public void show(TextView textView) {
        Memory t = this;
        int a = 1;
        while (t.hasnext()) {
            t = t.next;
            textView.append("空闲区" + a + " [起始地址=" + (t.begin+1) +
                    ", 结束地址=" + t.end + ", 大小=" + t.size + "]\n");
            a++;
        }
    }

    // 起始地址冒泡排序
    public void bubbleSortForAddress() {
        if (this == null || this.next == null) {
        } else {
            Memory cur = null, tail = null;
            cur = this.next;
            while (cur.next != tail) {
                while (cur.next != tail) {
                    if (cur.begin > cur.next.begin) {
                        int tmp_size = cur.size;
                        int tmp_begin = cur.begin;
                        cur.size = cur.next.size;
                        cur.begin = cur.next.begin;
                        cur.next.size = tmp_size;
                        cur.next.begin = tmp_begin;
                    }
                    cur = cur.next;
                }
                tail = cur;
                cur = this.next;
            }
        }

    }
}
