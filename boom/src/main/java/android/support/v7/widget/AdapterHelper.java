// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import java.util.List;
import java.util.Collection;
import java.util.Collections;
import android.support.v4.util.Pools;
import java.util.ArrayList;

class AdapterHelper implements OpReorderer.Callback
{
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pools.Pool<UpdateOp> mUpdateOpPool;
    
    AdapterHelper(final Callback callback) {
        this(callback, false);
    }
    
    AdapterHelper(final Callback mCallback, final boolean mDisableRecycler) {
        this.mUpdateOpPool = new Pools.SimplePool<UpdateOp>(30);
        this.mPendingUpdates = new ArrayList<UpdateOp>();
        this.mPostponedList = new ArrayList<UpdateOp>();
        this.mExistingUpdateTypes = 0;
        this.mCallback = mCallback;
        this.mDisableRecycler = mDisableRecycler;
        this.mOpReorderer = new OpReorderer((OpReorderer.Callback)this);
    }
    
    private void applyAdd(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyMove(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyRemove(final UpdateOp updateOp) {
        final int positionStart = updateOp.positionStart;
        int n = 0;
        int n2 = updateOp.positionStart + updateOp.itemCount;
        int n3 = -1;
        int n7;
        int n8;
        for (int i = updateOp.positionStart; i < n2; ++i, n = n8, n3 = n7) {
            int n4 = 0;
            int n5 = 0;
            if (this.mCallback.findViewHolder(i) != null || this.canFindInPreLayout(i)) {
                if (n3 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n, null));
                    n5 = 1;
                }
                final int n6 = 1;
                n4 = n5;
                n7 = n6;
            }
            else {
                if (n3 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n, null));
                    n4 = 1;
                }
                n7 = 0;
            }
            if (n4 != 0) {
                i -= n;
                n2 -= n;
                n8 = 1;
            }
            else {
                n8 = n + 1;
            }
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n != updateOp.itemCount) {
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(2, positionStart, n, null);
        }
        if (n3 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
        }
        else {
            this.postponeAndUpdateViewHolders(obtainUpdateOp);
        }
    }
    
    private void applyUpdate(final UpdateOp updateOp) {
        int positionStart = updateOp.positionStart;
        int n = 0;
        final int positionStart2 = updateOp.positionStart;
        final int itemCount = updateOp.itemCount;
        int n2 = -1;
        int n5;
        for (int i = updateOp.positionStart; i < positionStart2 + itemCount; ++i, n2 = n5) {
            int n6;
            if (this.mCallback.findViewHolder(i) != null || this.canFindInPreLayout(i)) {
                int n3 = n;
                int n4 = positionStart;
                if (n2 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(4, positionStart, n, updateOp.payload));
                    n3 = 0;
                    n4 = i;
                }
                n5 = 1;
                positionStart = n4;
                n6 = n3;
            }
            else {
                n6 = n;
                int n7 = positionStart;
                if (n2 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(4, positionStart, n, updateOp.payload));
                    n6 = 0;
                    n7 = i;
                }
                final int n8 = 0;
                positionStart = n7;
                n5 = n8;
            }
            n = n6 + 1;
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n != updateOp.itemCount) {
            final Object payload = updateOp.payload;
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(4, positionStart, n, payload);
        }
        if (n2 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
        }
        else {
            this.postponeAndUpdateViewHolders(obtainUpdateOp);
        }
    }
    
    private boolean canFindInPreLayout(final int n) {
        final boolean b = true;
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (this.findPositionOffset(updateOp.itemCount, i + 1) == n) {
                    return b;
                }
            }
            else if (updateOp.cmd == 1) {
                for (int positionStart = updateOp.positionStart, itemCount = updateOp.itemCount, j = updateOp.positionStart; j < positionStart + itemCount; ++j) {
                    final boolean b2 = b;
                    if (this.findPositionOffset(j, i + 1) == n) {
                        return b2;
                    }
                }
            }
        }
        return false;
    }
    
    private void dispatchAndUpdateViewHolders(UpdateOp obtainUpdateOp) {
        if (obtainUpdateOp.cmd == 1 || obtainUpdateOp.cmd == 8) {
            throw new IllegalArgumentException("should not dispatch add or move for pre layout");
        }
        int updatePositionWithPostponed = this.updatePositionWithPostponed(obtainUpdateOp.positionStart, obtainUpdateOp.cmd);
        int n = 1;
        int positionStart = obtainUpdateOp.positionStart;
        int n2 = 0;
        switch (obtainUpdateOp.cmd) {
            default: {
                throw new IllegalArgumentException("op should be remove or update." + obtainUpdateOp);
            }
            case 4: {
                n2 = 1;
                break;
            }
            case 2: {
                n2 = 0;
                break;
            }
        }
        int i = 1;
        while (i < obtainUpdateOp.itemCount) {
            final int updatePositionWithPostponed2 = this.updatePositionWithPostponed(obtainUpdateOp.positionStart + n2 * i, obtainUpdateOp.cmd);
            int n3 = 0;
            Label_0184: {
                switch (obtainUpdateOp.cmd) {
                    default: {
                        n3 = n3;
                        break Label_0184;
                    }
                    case 2: {
                        if (updatePositionWithPostponed2 == updatePositionWithPostponed) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                        break Label_0184;
                    }
                    case 4: {
                        if (updatePositionWithPostponed2 == updatePositionWithPostponed + 1) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                    }
                    case 3: {
                        int n4;
                        if (n3 != 0) {
                            n4 = n + 1;
                        }
                        else {
                            final UpdateOp obtainUpdateOp2 = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n, obtainUpdateOp.payload);
                            this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp2, positionStart);
                            this.recycleUpdateOp(obtainUpdateOp2);
                            int n5 = positionStart;
                            if (obtainUpdateOp.cmd == 4) {
                                n5 = positionStart + n;
                            }
                            updatePositionWithPostponed = updatePositionWithPostponed2;
                            final int n6 = 1;
                            positionStart = n5;
                            n4 = n6;
                        }
                        ++i;
                        n = n4;
                        continue;
                    }
                }
            }
        }
        final Object payload = obtainUpdateOp.payload;
        this.recycleUpdateOp(obtainUpdateOp);
        if (n > 0) {
            obtainUpdateOp = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n, payload);
            this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp, positionStart);
            this.recycleUpdateOp(obtainUpdateOp);
        }
    }
    
    private void postponeAndUpdateViewHolders(final UpdateOp updateOp) {
        this.mPostponedList.add(updateOp);
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("Unknown update op type for " + updateOp);
            }
            case 1: {
                this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                break;
            }
            case 8: {
                this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                break;
            }
            case 2: {
                this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(updateOp.positionStart, updateOp.itemCount);
                break;
            }
            case 4: {
                this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                break;
            }
        }
    }
    
    private int updatePositionWithPostponed(int i, final int n) {
        int j = this.mPostponedList.size() - 1;
        int n2 = i;
        while (j >= 0) {
            final UpdateOp updateOp = this.mPostponedList.get(j);
            if (updateOp.cmd == 8) {
                int n3;
                if (updateOp.positionStart < updateOp.itemCount) {
                    i = updateOp.positionStart;
                    n3 = updateOp.itemCount;
                }
                else {
                    i = updateOp.itemCount;
                    n3 = updateOp.positionStart;
                }
                if (n2 >= i && n2 <= n3) {
                    if (i == updateOp.positionStart) {
                        if (n == 1) {
                            ++updateOp.itemCount;
                        }
                        else if (n == 2) {
                            --updateOp.itemCount;
                        }
                        i = n2 + 1;
                    }
                    else {
                        if (n == 1) {
                            ++updateOp.positionStart;
                        }
                        else if (n == 2) {
                            --updateOp.positionStart;
                        }
                        i = n2 - 1;
                    }
                }
                else if ((i = n2) < updateOp.positionStart) {
                    if (n == 1) {
                        ++updateOp.positionStart;
                        ++updateOp.itemCount;
                        i = n2;
                    }
                    else {
                        i = n2;
                        if (n == 2) {
                            --updateOp.positionStart;
                            --updateOp.itemCount;
                            i = n2;
                        }
                    }
                }
            }
            else if (updateOp.positionStart <= n2) {
                if (updateOp.cmd == 1) {
                    i = n2 - updateOp.itemCount;
                }
                else {
                    i = n2;
                    if (updateOp.cmd == 2) {
                        i = n2 + updateOp.itemCount;
                    }
                }
            }
            else if (n == 1) {
                ++updateOp.positionStart;
                i = n2;
            }
            else {
                i = n2;
                if (n == 2) {
                    --updateOp.positionStart;
                    i = n2;
                }
            }
            --j;
            n2 = i;
        }
        UpdateOp updateOp2;
        for (i = this.mPostponedList.size() - 1; i >= 0; --i) {
            updateOp2 = this.mPostponedList.get(i);
            if (updateOp2.cmd == 8) {
                if (updateOp2.itemCount == updateOp2.positionStart || updateOp2.itemCount < 0) {
                    this.mPostponedList.remove(i);
                    this.recycleUpdateOp(updateOp2);
                }
            }
            else if (updateOp2.itemCount <= 0) {
                this.mPostponedList.remove(i);
                this.recycleUpdateOp(updateOp2);
            }
        }
        return n2;
    }
    
    AdapterHelper addUpdateOp(final UpdateOp... elements) {
        Collections.addAll(this.mPendingUpdates, elements);
        return this;
    }
    
    public int applyPendingUpdatesToPosition(int itemCount) {
        final int size = this.mPendingUpdates.size();
        int index = 0;
        int n = itemCount;
    Label_0140:
        while (true) {
            itemCount = n;
            if (index >= size) {
                break;
            }
            final UpdateOp updateOp = this.mPendingUpdates.get(index);
            switch (updateOp.cmd) {
                default: {
                    itemCount = n;
                    break;
                }
                case 1: {
                    itemCount = n;
                    if (updateOp.positionStart <= n) {
                        itemCount = n + updateOp.itemCount;
                        break;
                    }
                    break;
                }
                case 2: {
                    itemCount = n;
                    if (updateOp.positionStart > n) {
                        break;
                    }
                    if (updateOp.positionStart + updateOp.itemCount > n) {
                        itemCount = -1;
                        break Label_0140;
                    }
                    itemCount = n - updateOp.itemCount;
                    break;
                }
                case 8: {
                    if (updateOp.positionStart == n) {
                        itemCount = updateOp.itemCount;
                        break;
                    }
                    int n2;
                    if (updateOp.positionStart < (n2 = n)) {
                        n2 = n - 1;
                    }
                    if (updateOp.itemCount <= (itemCount = n2)) {
                        itemCount = n2 + 1;
                        break;
                    }
                    break;
                }
            }
            ++index;
            n = itemCount;
        }
        return itemCount;
    }
    
    void consumePostponedUpdates() {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            this.mCallback.onDispatchSecondPass((UpdateOp)this.mPostponedList.get(i));
        }
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
    
    void consumeUpdatesInOnePass() {
        this.consumePostponedUpdates();
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 1: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
                case 2: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
                case 4: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                    break;
                }
                case 8: {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                    break;
                }
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.mExistingUpdateTypes = 0;
    }
    
    void dispatchFirstPassAndUpdateViewHolders(final UpdateOp updateOp, final int n) {
        this.mCallback.onDispatchFirstPass(updateOp);
        switch (updateOp.cmd) {
            default: {
                throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
            }
            case 2: {
                this.mCallback.offsetPositionsForRemovingInvisible(n, updateOp.itemCount);
                break;
            }
            case 4: {
                this.mCallback.markViewHoldersUpdated(n, updateOp.itemCount, updateOp.payload);
                break;
            }
        }
    }
    
    int findPositionOffset(final int n) {
        return this.findPositionOffset(n, 0);
    }
    
    int findPositionOffset(int itemCount, int n) {
        final int size = this.mPostponedList.size();
        int index = n;
        n = itemCount;
        while (true) {
            itemCount = n;
            if (index >= size) {
                break;
            }
            final UpdateOp updateOp = this.mPostponedList.get(index);
            if (updateOp.cmd == 8) {
                if (updateOp.positionStart == n) {
                    itemCount = updateOp.itemCount;
                }
                else {
                    int n2;
                    if (updateOp.positionStart < (n2 = n)) {
                        n2 = n - 1;
                    }
                    if (updateOp.itemCount <= (itemCount = n2)) {
                        itemCount = n2 + 1;
                    }
                }
            }
            else if (updateOp.positionStart <= (itemCount = n)) {
                if (updateOp.cmd == 2) {
                    if (n < updateOp.positionStart + updateOp.itemCount) {
                        itemCount = -1;
                        break;
                    }
                    itemCount = n - updateOp.itemCount;
                }
                else {
                    itemCount = n;
                    if (updateOp.cmd == 1) {
                        itemCount = n + updateOp.itemCount;
                    }
                }
            }
            ++index;
            n = itemCount;
        }
        return itemCount;
    }
    
    boolean hasAnyUpdateTypes(final int n) {
        return (this.mExistingUpdateTypes & n) != 0x0;
    }
    
    boolean hasPendingUpdates() {
        return this.mPendingUpdates.size() > 0;
    }
    
    boolean hasUpdates() {
        return !this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty();
    }
    
    @Override
    public UpdateOp obtainUpdateOp(final int cmd, final int positionStart, final int itemCount, final Object payload) {
        final UpdateOp updateOp = this.mUpdateOpPool.acquire();
        UpdateOp updateOp2;
        if (updateOp == null) {
            updateOp2 = new UpdateOp(cmd, positionStart, itemCount, payload);
        }
        else {
            updateOp.cmd = cmd;
            updateOp.positionStart = positionStart;
            updateOp.itemCount = itemCount;
            updateOp.payload = payload;
            updateOp2 = updateOp;
        }
        return updateOp2;
    }
    
    boolean onItemRangeChanged(final int n, final int n2, final Object o) {
        boolean b = true;
        this.mPendingUpdates.add(this.obtainUpdateOp(4, n, n2, o));
        this.mExistingUpdateTypes |= 0x4;
        if (this.mPendingUpdates.size() != 1) {
            b = false;
        }
        return b;
    }
    
    boolean onItemRangeInserted(final int n, final int n2) {
        boolean b = true;
        this.mPendingUpdates.add(this.obtainUpdateOp(1, n, n2, null));
        this.mExistingUpdateTypes |= 0x1;
        if (this.mPendingUpdates.size() != 1) {
            b = false;
        }
        return b;
    }
    
    boolean onItemRangeMoved(final int n, final int n2, final int n3) {
        final boolean b = false;
        boolean b2 = true;
        if (n == n2) {
            b2 = b;
        }
        else {
            if (n3 != 1) {
                throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
            }
            this.mPendingUpdates.add(this.obtainUpdateOp(8, n, n2, null));
            this.mExistingUpdateTypes |= 0x8;
            if (this.mPendingUpdates.size() != 1) {
                b2 = false;
            }
        }
        return b2;
    }
    
    boolean onItemRangeRemoved(final int n, final int n2) {
        boolean b = true;
        this.mPendingUpdates.add(this.obtainUpdateOp(2, n, n2, null));
        this.mExistingUpdateTypes |= 0x2;
        if (this.mPendingUpdates.size() != 1) {
            b = false;
        }
        return b;
    }
    
    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            switch (updateOp.cmd) {
                case 1: {
                    this.applyAdd(updateOp);
                    break;
                }
                case 2: {
                    this.applyRemove(updateOp);
                    break;
                }
                case 4: {
                    this.applyUpdate(updateOp);
                    break;
                }
                case 8: {
                    this.applyMove(updateOp);
                    break;
                }
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.mPendingUpdates.clear();
    }
    
    @Override
    public void recycleUpdateOp(final UpdateOp updateOp) {
        if (!this.mDisableRecycler) {
            updateOp.payload = null;
            this.mUpdateOpPool.release(updateOp);
        }
    }
    
    void recycleUpdateOpsAndClearList(final List<UpdateOp> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            this.recycleUpdateOp(list.get(i));
        }
        list.clear();
    }
    
    void reset() {
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
    
    interface Callback
    {
        RecyclerView.ViewHolder findViewHolder(final int p0);
        
        void markViewHoldersUpdated(final int p0, final int p1, final Object p2);
        
        void offsetPositionsForAdd(final int p0, final int p1);
        
        void offsetPositionsForMove(final int p0, final int p1);
        
        void offsetPositionsForRemovingInvisible(final int p0, final int p1);
        
        void offsetPositionsForRemovingLaidOutOrNewView(final int p0, final int p1);
        
        void onDispatchFirstPass(final UpdateOp p0);
        
        void onDispatchSecondPass(final UpdateOp p0);
    }
    
    static class UpdateOp
    {
        static final int ADD = 1;
        static final int MOVE = 8;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 2;
        static final int UPDATE = 4;
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;
        
        UpdateOp(final int cmd, final int positionStart, final int itemCount, final Object payload) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }
        
        String cmdToString() {
            String s = null;
            switch (this.cmd) {
                default: {
                    s = "??";
                    break;
                }
                case 1: {
                    s = "add";
                    break;
                }
                case 2: {
                    s = "rm";
                    break;
                }
                case 4: {
                    s = "up";
                    break;
                }
                case 8: {
                    s = "mv";
                    break;
                }
            }
            return s;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = true;
            boolean b2;
            if (this == o) {
                b2 = b;
            }
            else if (o == null || this.getClass() != o.getClass()) {
                b2 = false;
            }
            else {
                final UpdateOp updateOp = (UpdateOp)o;
                if (this.cmd != updateOp.cmd) {
                    b2 = false;
                }
                else {
                    if (this.cmd == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == updateOp.positionStart) {
                        b2 = b;
                        if (this.positionStart == updateOp.itemCount) {
                            return b2;
                        }
                    }
                    if (this.itemCount != updateOp.itemCount) {
                        b2 = false;
                    }
                    else if (this.positionStart != updateOp.positionStart) {
                        b2 = false;
                    }
                    else if (this.payload != null) {
                        b2 = b;
                        if (!this.payload.equals(updateOp.payload)) {
                            b2 = false;
                        }
                    }
                    else {
                        b2 = b;
                        if (updateOp.payload != null) {
                            b2 = false;
                        }
                    }
                }
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
        }
        
        @Override
        public String toString() {
            return Integer.toHexString(System.identityHashCode(this)) + "[" + this.cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + ",p:" + this.payload + "]";
        }
    }
}
