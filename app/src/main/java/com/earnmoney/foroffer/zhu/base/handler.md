请解释Handler 机制(tip : Handler looper message 之间关系,如何关联到一起的,postdelaymessage 如何入队的,
                       消息队列是什么样的(阻塞 or 非阻塞),使用场景注意事项 等请把你知道的一切用文字描述出来)
                       
Handler机制是android最核心的通讯机制,主要用来提供workThread与UIThread的间的通讯.

Hanlder:用来将message放到MessageQueue中以及,接收looper去出的message
Looper:每个线程只有一个,通过TheadLocal保证,UI线程默认创建了looper,子线程需要用looper.prepare()创建,用于循环从MessageQueue中取message.
MessageQueue:消息队列,先进先出,阻塞队列    

sendMessageDelayed:是用当前系统时间+delay 赋值给message的when属性,
    然后遍历消息队列,找到一个when大于当前消息message将此消息插入到,找到的消息前面.如果未找到则插在队尾.



源代码见 android.os.MessageQueue 536行  sdk版本28

 boolean enqueueMessage(Message msg, long when) {
        if (msg.target == null) {
            throw new IllegalArgumentException("Message must have a target.");
        }
        if (msg.isInUse()) {
            throw new IllegalStateException(msg + " This message is already in use.");
        }

        synchronized (this) {
            if (mQuitting) {
                IllegalStateException e = new IllegalStateException(
                        msg.target + " sending message to a Handler on a dead thread");
                Log.w(TAG, e.getMessage(), e);
                msg.recycle();
                return false;
            }

            msg.markInUse();
            msg.when = when;
            Message p = mMessages;
            boolean needWake;
            if (p == null || when == 0 || when < p.when) {
                // New head, wake up the event queue if blocked.
                msg.next = p;
                mMessages = msg;
                needWake = mBlocked;
            } else {
                // Inserted within the middle of the queue.  Usually we don't have to wake
                // up the event queue unless there is a barrier at the head of the queue
                // and the message is the earliest asynchronous message in the queue.
                needWake = mBlocked && p.target == null && msg.isAsynchronous();
                Message prev;
                for (;;) {
                    prev = p;
                    p = p.next;
                    if (p == null || when < p.when) {
                        break;
                    }
                    if (needWake && p.isAsynchronous()) {
                        needWake = false;
                    }
                }
                msg.next = p; // invariant: p == prev.next
                prev.next = msg;
            }

            // We can assume mPtr != 0 because mQuitting is false.
            if (needWake) {
                nativeWake(mPtr);
            }
        }
        return true;
    }   