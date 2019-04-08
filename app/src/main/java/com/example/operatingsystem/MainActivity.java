package com.example.operatingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final PCB ready_head = new PCB("head", 0);
    final PCB block_head = new PCB("head", 0);
    final PCB run_head = new PCB("head", 0);
    final Memory memory_head = new Memory(-1, 0);
    // Memory p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int s = Integer.parseInt(intent.getStringExtra("all"));
        int size = Integer.parseInt(intent.getStringExtra("user"));
        final TextView textView = findViewById(R.id.textView);
        final EditText editText = findViewById(R.id.name);
        final EditText editText1 = findViewById(R.id.size);
        Button button = findViewById(R.id.button);
        Button button1 = findViewById(R.id.button2);
        Button button2 = findViewById(R.id.button3);
        Button button3 = findViewById(R.id.button4);
        Button button4 = findViewById(R.id.button6);
        textView.setText("您使用的电脑内存空间大小为" + s + "KB\n" + "系统区内存地址为1KB——" + size +
                "KB,大小为" + size + "KB,用户区内存地址为" + (size + 1) + "KB——" + s + "KB,大小为"
                + (s - size) + "KB");
        ready_head.setNext(null);
        block_head.setNext(null);
        run_head.setNext(null);
        final Memory p = new Memory(size, s - size);
        memory_head.setNext(p);
        p.setBefore(memory_head);
        //创建进程
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(editText1.getText().toString());
                PCB pcb = new PCB(editText.getText().toString(), size);
                if (memory_head.getNext().getSize() == 0) {
                    textView.setText("系统无空闲区！\n");
                }else {
                    if (run_head.hasNext()) {
                        //Create_PCB(pcb);
                        int a=memory_head.addPCB(pcb, textView);
                        if(a==1) {
                            ready_head.addToTail(pcb);
                            show(ready_head, block_head, run_head, memory_head, textView);
                        }
                    }
                    if (!ready_head.hasNext()) {
                        // Create_PCB(pcb);
                        pcb.setEnd(pcb.getBegin() + pcb.getSize());
                        int a=memory_head.addPCB(pcb, textView);
                        if(a==1) {
                            run_head.setNext(pcb);
                            show(ready_head, block_head, run_head, memory_head, textView);
                        }
                    }

                }
            }
        });
        //执行进程时间片到 从执行队列进就绪队列尾 就绪队列头进执行队列
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ready_head.addToTail(run_head.getNext());
                run_head.setNext(ready_head.deQueue(textView));
                show(ready_head, block_head, run_head, memory_head, textView);
            }
        });
        //阻塞执行进程 执行队列进阻塞队尾 就绪队头进执行队
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block_head.addToTail(run_head.getNext());
                run_head.setNext(ready_head.deQueue(textView));
                show(ready_head, block_head, run_head, memory_head, textView);
            }
        });
        //唤醒第一个阻塞进程 阻塞队头进就绪队尾
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PCB pcb=block_head.deQueue(textView);
                if (pcb == null) {
                    textView.setText("当前无阻塞进程！");
                } else {
                    if (run_head.getNext() == null) {
                        run_head.setNext(pcb);

                    } else {
                        ready_head.addToTail(pcb);
                    }
                    show(ready_head, block_head, run_head, memory_head, textView);
                }
            }
        });
        //终止执行进程 就绪队头进执行队 内存重新分配
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run_head.getNext() == null) {
                    textView.setText("当前无可终止进程！");
                } else {
                    memory_head.endPCB(run_head.getNext());
                    run_head.setNext(ready_head.deQueue(textView));
                    show(ready_head, block_head, run_head, memory_head, textView);
                }
            }
        });
        //显示
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(ready_head, block_head, run_head, memory_head, textView);
            }
        });
    }

    public static void show(PCB ready_head, PCB block_head, PCB run_head, Memory memory_head, TextView textView) {
        textView.setText("");
        textView.append("执行队列：\n");
        if (run_head.getNext() == null) {
            textView.append("无执行进程！\n");
        } else {
            textView.append("PCB [name=" + run_head.getNext().getName() + ",起始地址="
                    + (run_head.getNext().getBegin() + 1) + ",结束地址=" + run_head.getNext().getEnd() + ",大小="
                    + run_head.getNext().getSize() + "]\n");
        }
        textView.append("就绪队列：\n");
        if (ready_head.getNext() == null) {
            textView.append("就绪队列为空！\n");
        } else {
            ready_head.show(textView);
        }
        textView.append("阻塞队列：\n");
        if (block_head.getNext() == null) {
            textView.append("阻塞队列为空！\n");
        } else {
            block_head.show(textView);
        }
        textView.append("内存空闲区\n");
        if (memory_head.getNext().getSize() == 0) {
            textView.append("系统无空闲区！\n");
        } else {
            memory_head.show(textView);
        }
    }
}
