// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import java.util.Iterator;
import java.util.Collection;
import android.support.annotation.NonNull;
import android.support.v4.animation.AnimatorCompatHelper;
import java.util.List;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewCompat;
import java.util.ArrayList;

public class DefaultItemAnimator extends SimpleItemAnimator
{
    private static final boolean DEBUG = false;
    private ArrayList<ViewHolder> mAddAnimations;
    private ArrayList<ArrayList<ViewHolder>> mAdditionsList;
    private ArrayList<ViewHolder> mChangeAnimations;
    private ArrayList<ArrayList<ChangeInfo>> mChangesList;
    private ArrayList<ViewHolder> mMoveAnimations;
    private ArrayList<ArrayList<MoveInfo>> mMovesList;
    private ArrayList<ViewHolder> mPendingAdditions;
    private ArrayList<ChangeInfo> mPendingChanges;
    private ArrayList<MoveInfo> mPendingMoves;
    private ArrayList<ViewHolder> mPendingRemovals;
    private ArrayList<ViewHolder> mRemoveAnimations;
    
    public DefaultItemAnimator() {
        this.mPendingRemovals = new ArrayList<ViewHolder>();
        this.mPendingAdditions = new ArrayList<ViewHolder>();
        this.mPendingMoves = new ArrayList<MoveInfo>();
        this.mPendingChanges = new ArrayList<ChangeInfo>();
        this.mAdditionsList = new ArrayList<ArrayList<ViewHolder>>();
        this.mMovesList = new ArrayList<ArrayList<MoveInfo>>();
        this.mChangesList = new ArrayList<ArrayList<ChangeInfo>>();
        this.mAddAnimations = new ArrayList<ViewHolder>();
        this.mMoveAnimations = new ArrayList<ViewHolder>();
        this.mRemoveAnimations = new ArrayList<ViewHolder>();
        this.mChangeAnimations = new ArrayList<ViewHolder>();
    }
    
    private void animateAddImpl(final ViewHolder e) {
        final ViewPropertyAnimatorCompat animate = ViewCompat.animate(e.itemView);
        this.mAddAnimations.add(e);
        animate.alpha(1.0f).setDuration(((RecyclerView.ItemAnimator)this).getAddDuration()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationCancel(final View view) {
                ViewCompat.setAlpha(view, 1.0f);
            }
            
            @Override
            public void onAnimationEnd(final View view) {
                animate.setListener(null);
                DefaultItemAnimator.this.dispatchAddFinished(e);
                DefaultItemAnimator.this.mAddAnimations.remove(e);
                DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            @Override
            public void onAnimationStart(final View view) {
                DefaultItemAnimator.this.dispatchAddStarting(e);
            }
        }).start();
    }
    
    private void animateChangeImpl(final ChangeInfo changeInfo) {
        final ViewHolder oldHolder = changeInfo.oldHolder;
        View itemView;
        if (oldHolder == null) {
            itemView = null;
        }
        else {
            itemView = oldHolder.itemView;
        }
        final ViewHolder newHolder = changeInfo.newHolder;
        View itemView2;
        if (newHolder != null) {
            itemView2 = newHolder.itemView;
        }
        else {
            itemView2 = null;
        }
        if (itemView != null) {
            final ViewPropertyAnimatorCompat setDuration = ViewCompat.animate(itemView).setDuration(((RecyclerView.ItemAnimator)this).getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            setDuration.translationX((float)(changeInfo.toX - changeInfo.fromX));
            setDuration.translationY((float)(changeInfo.toY - changeInfo.fromY));
            setDuration.alpha(0.0f).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationEnd(final View view) {
                    setDuration.setListener(null);
                    ViewCompat.setAlpha(view, 1.0f);
                    ViewCompat.setTranslationX(view, 0.0f);
                    ViewCompat.setTranslationY(view, 0.0f);
                    DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
                    DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder);
                    DefaultItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                @Override
                public void onAnimationStart(final View view) {
                    DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
                }
            }).start();
        }
        if (itemView2 != null) {
            final ViewPropertyAnimatorCompat animate = ViewCompat.animate(itemView2);
            this.mChangeAnimations.add(changeInfo.newHolder);
            animate.translationX(0.0f).translationY(0.0f).setDuration(((RecyclerView.ItemAnimator)this).getChangeDuration()).alpha(1.0f).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationEnd(final View view) {
                    animate.setListener(null);
                    ViewCompat.setAlpha(itemView2, 1.0f);
                    ViewCompat.setTranslationX(itemView2, 0.0f);
                    ViewCompat.setTranslationY(itemView2, 0.0f);
                    DefaultItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
                    DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder);
                    DefaultItemAnimator.this.dispatchFinishedWhenDone();
                }
                
                @Override
                public void onAnimationStart(final View view) {
                    DefaultItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
                }
            }).start();
        }
    }
    
    private void animateMoveImpl(final ViewHolder e, int n, int n2, final int n3, final int n4) {
        final View itemView = e.itemView;
        n = n3 - n;
        n2 = n4 - n2;
        if (n != 0) {
            ViewCompat.animate(itemView).translationX(0.0f);
        }
        if (n2 != 0) {
            ViewCompat.animate(itemView).translationY(0.0f);
        }
        final ViewPropertyAnimatorCompat animate = ViewCompat.animate(itemView);
        this.mMoveAnimations.add(e);
        animate.setDuration(((RecyclerView.ItemAnimator)this).getMoveDuration()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationCancel(final View view) {
                if (n != 0) {
                    ViewCompat.setTranslationX(view, 0.0f);
                }
                if (n2 != 0) {
                    ViewCompat.setTranslationY(view, 0.0f);
                }
            }
            
            @Override
            public void onAnimationEnd(final View view) {
                animate.setListener(null);
                DefaultItemAnimator.this.dispatchMoveFinished(e);
                DefaultItemAnimator.this.mMoveAnimations.remove(e);
                DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            @Override
            public void onAnimationStart(final View view) {
                DefaultItemAnimator.this.dispatchMoveStarting(e);
            }
        }).start();
    }
    
    private void animateRemoveImpl(final ViewHolder e) {
        final ViewPropertyAnimatorCompat animate = ViewCompat.animate(e.itemView);
        this.mRemoveAnimations.add(e);
        animate.setDuration(((RecyclerView.ItemAnimator)this).getRemoveDuration()).alpha(0.0f).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationEnd(final View view) {
                animate.setListener(null);
                ViewCompat.setAlpha(view, 1.0f);
                DefaultItemAnimator.this.dispatchRemoveFinished(e);
                DefaultItemAnimator.this.mRemoveAnimations.remove(e);
                DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }
            
            @Override
            public void onAnimationStart(final View view) {
                DefaultItemAnimator.this.dispatchRemoveStarting(e);
            }
        }).start();
    }
    
    private void dispatchFinishedWhenDone() {
        if (!this.isRunning()) {
            ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
        }
    }
    
    private void endChangeAnimation(final List<ChangeInfo> list, final ViewHolder viewHolder) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final ChangeInfo changeInfo = list.get(i);
            if (this.endChangeAnimationIfNecessary(changeInfo, viewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                list.remove(changeInfo);
            }
        }
    }
    
    private void endChangeAnimationIfNecessary(final ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }
    
    private boolean endChangeAnimationIfNecessary(final ChangeInfo changeInfo, final ViewHolder viewHolder) {
        boolean b = false;
        if (changeInfo.newHolder == viewHolder) {
            changeInfo.newHolder = null;
        }
        else {
            if (changeInfo.oldHolder != viewHolder) {
                return false;
            }
            changeInfo.oldHolder = null;
            b = true;
        }
        ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        ViewCompat.setTranslationX(viewHolder.itemView, 0.0f);
        ViewCompat.setTranslationY(viewHolder.itemView, 0.0f);
        this.dispatchChangeFinished(viewHolder, b);
        return true;
    }
    
    private void resetAnimation(final ViewHolder viewHolder) {
        AnimatorCompatHelper.clearInterpolator(viewHolder.itemView);
        this.endAnimation(viewHolder);
    }
    
    @Override
    public boolean animateAdd(final ViewHolder e) {
        this.resetAnimation(e);
        ViewCompat.setAlpha(e.itemView, 0.0f);
        this.mPendingAdditions.add(e);
        return true;
    }
    
    @Override
    public boolean animateChange(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int n, final int n2, final int n3, final int n4) {
        boolean animateMove;
        if (viewHolder == viewHolder2) {
            animateMove = this.animateMove(viewHolder, n, n2, n3, n4);
        }
        else {
            final float translationX = ViewCompat.getTranslationX(viewHolder.itemView);
            final float translationY = ViewCompat.getTranslationY(viewHolder.itemView);
            final float alpha = ViewCompat.getAlpha(viewHolder.itemView);
            this.resetAnimation(viewHolder);
            final int n5 = (int)(n3 - n - translationX);
            final int n6 = (int)(n4 - n2 - translationY);
            ViewCompat.setTranslationX(viewHolder.itemView, translationX);
            ViewCompat.setTranslationY(viewHolder.itemView, translationY);
            ViewCompat.setAlpha(viewHolder.itemView, alpha);
            if (viewHolder2 != null) {
                this.resetAnimation(viewHolder2);
                ViewCompat.setTranslationX(viewHolder2.itemView, (float)(-n5));
                ViewCompat.setTranslationY(viewHolder2.itemView, (float)(-n6));
                ViewCompat.setAlpha(viewHolder2.itemView, 0.0f);
            }
            this.mPendingChanges.add(new ChangeInfo(viewHolder, viewHolder2, n, n2, n3, n4));
            animateMove = true;
        }
        return animateMove;
    }
    
    @Override
    public boolean animateMove(final ViewHolder viewHolder, int n, int n2, final int n3, final int n4) {
        final View itemView = viewHolder.itemView;
        n += (int)ViewCompat.getTranslationX(viewHolder.itemView);
        final int n5 = (int)(n2 + ViewCompat.getTranslationY(viewHolder.itemView));
        this.resetAnimation(viewHolder);
        final int n6 = n3 - n;
        n2 = n4 - n5;
        boolean b;
        if (n6 == 0 && n2 == 0) {
            this.dispatchMoveFinished(viewHolder);
            b = false;
        }
        else {
            if (n6 != 0) {
                ViewCompat.setTranslationX(itemView, (float)(-n6));
            }
            if (n2 != 0) {
                ViewCompat.setTranslationY(itemView, (float)(-n2));
            }
            this.mPendingMoves.add(new MoveInfo(viewHolder, n, n5, n3, n4));
            b = true;
        }
        return b;
    }
    
    @Override
    public boolean animateRemove(final ViewHolder e) {
        this.resetAnimation(e);
        this.mPendingRemovals.add(e);
        return true;
    }
    
    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder, @NonNull final List<Object> list) {
        return !list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list);
    }
    
    void cancelAll(final List<ViewHolder> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            ViewCompat.animate(list.get(i).itemView).cancel();
        }
    }
    
    @Override
    public void endAnimation(final ViewHolder o) {
        final View itemView = o.itemView;
        ViewCompat.animate(itemView).cancel();
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            if (this.mPendingMoves.get(i).holder == o) {
                ViewCompat.setTranslationY(itemView, 0.0f);
                ViewCompat.setTranslationX(itemView, 0.0f);
                this.dispatchMoveFinished(o);
                this.mPendingMoves.remove(i);
            }
        }
        this.endChangeAnimation(this.mPendingChanges, o);
        if (this.mPendingRemovals.remove(o)) {
            ViewCompat.setAlpha(itemView, 1.0f);
            this.dispatchRemoveFinished(o);
        }
        if (this.mPendingAdditions.remove(o)) {
            ViewCompat.setAlpha(itemView, 1.0f);
            this.dispatchAddFinished(o);
        }
        for (int j = this.mChangesList.size() - 1; j >= 0; --j) {
            final ArrayList<ChangeInfo> list = this.mChangesList.get(j);
            this.endChangeAnimation(list, o);
            if (list.isEmpty()) {
                this.mChangesList.remove(j);
            }
        }
        for (int k = this.mMovesList.size() - 1; k >= 0; --k) {
            final ArrayList<MoveInfo> list2 = this.mMovesList.get(k);
            int l = list2.size() - 1;
            while (l >= 0) {
                if (list2.get(l).holder == o) {
                    ViewCompat.setTranslationY(itemView, 0.0f);
                    ViewCompat.setTranslationX(itemView, 0.0f);
                    this.dispatchMoveFinished(o);
                    list2.remove(l);
                    if (list2.isEmpty()) {
                        this.mMovesList.remove(k);
                        break;
                    }
                    break;
                }
                else {
                    --l;
                }
            }
        }
        for (int n = this.mAdditionsList.size() - 1; n >= 0; --n) {
            final ArrayList<ViewHolder> list3 = this.mAdditionsList.get(n);
            if (list3.remove(o)) {
                ViewCompat.setAlpha(itemView, 1.0f);
                this.dispatchAddFinished(o);
                if (list3.isEmpty()) {
                    this.mAdditionsList.remove(n);
                }
            }
        }
        if (this.mRemoveAnimations.remove(o)) {}
        if (this.mAddAnimations.remove(o)) {}
        if (this.mChangeAnimations.remove(o)) {}
        if (this.mMoveAnimations.remove(o)) {}
        this.dispatchFinishedWhenDone();
    }
    
    @Override
    public void endAnimations() {
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            final MoveInfo moveInfo = this.mPendingMoves.get(i);
            final View itemView = moveInfo.holder.itemView;
            ViewCompat.setTranslationY(itemView, 0.0f);
            ViewCompat.setTranslationX(itemView, 0.0f);
            this.dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(i);
        }
        for (int j = this.mPendingRemovals.size() - 1; j >= 0; --j) {
            this.dispatchRemoveFinished(this.mPendingRemovals.get(j));
            this.mPendingRemovals.remove(j);
        }
        for (int k = this.mPendingAdditions.size() - 1; k >= 0; --k) {
            final ViewHolder viewHolder = this.mPendingAdditions.get(k);
            ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
            this.dispatchAddFinished(viewHolder);
            this.mPendingAdditions.remove(k);
        }
        for (int l = this.mPendingChanges.size() - 1; l >= 0; --l) {
            this.endChangeAnimationIfNecessary(this.mPendingChanges.get(l));
        }
        this.mPendingChanges.clear();
        if (this.isRunning()) {
            for (int index = this.mMovesList.size() - 1; index >= 0; --index) {
                final ArrayList<MoveInfo> o = this.mMovesList.get(index);
                for (int n = o.size() - 1; n >= 0; --n) {
                    final MoveInfo moveInfo2 = o.get(n);
                    final View itemView2 = moveInfo2.holder.itemView;
                    ViewCompat.setTranslationY(itemView2, 0.0f);
                    ViewCompat.setTranslationX(itemView2, 0.0f);
                    this.dispatchMoveFinished(moveInfo2.holder);
                    o.remove(n);
                    if (o.isEmpty()) {
                        this.mMovesList.remove(o);
                    }
                }
            }
            for (int index2 = this.mAdditionsList.size() - 1; index2 >= 0; --index2) {
                final ArrayList<ViewHolder> o2 = this.mAdditionsList.get(index2);
                for (int n2 = o2.size() - 1; n2 >= 0; --n2) {
                    final ViewHolder viewHolder2 = o2.get(n2);
                    ViewCompat.setAlpha(viewHolder2.itemView, 1.0f);
                    this.dispatchAddFinished(viewHolder2);
                    o2.remove(n2);
                    if (o2.isEmpty()) {
                        this.mAdditionsList.remove(o2);
                    }
                }
            }
            for (int index3 = this.mChangesList.size() - 1; index3 >= 0; --index3) {
                final ArrayList<ChangeInfo> o3 = this.mChangesList.get(index3);
                for (int index4 = o3.size() - 1; index4 >= 0; --index4) {
                    this.endChangeAnimationIfNecessary(o3.get(index4));
                    if (o3.isEmpty()) {
                        this.mChangesList.remove(o3);
                    }
                }
            }
            this.cancelAll(this.mRemoveAnimations);
            this.cancelAll(this.mMoveAnimations);
            this.cancelAll(this.mAddAnimations);
            this.cancelAll(this.mChangeAnimations);
            ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
        }
    }
    
    @Override
    public boolean isRunning() {
        return !this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty();
    }
    
    @Override
    public void runPendingAnimations() {
        boolean b;
        if (!this.mPendingRemovals.isEmpty()) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if (!this.mPendingMoves.isEmpty()) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        boolean b3;
        if (!this.mPendingChanges.isEmpty()) {
            b3 = true;
        }
        else {
            b3 = false;
        }
        boolean b4;
        if (!this.mPendingAdditions.isEmpty()) {
            b4 = true;
        }
        else {
            b4 = false;
        }
        if (b || b2 || b4 || b3) {
            final Iterator<ViewHolder> iterator = this.mPendingRemovals.iterator();
            while (iterator.hasNext()) {
                this.animateRemoveImpl(iterator.next());
            }
            this.mPendingRemovals.clear();
            if (b2) {
                final ArrayList<MoveInfo> e = new ArrayList<MoveInfo>();
                e.addAll(this.mPendingMoves);
                this.mMovesList.add(e);
                this.mPendingMoves.clear();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (final MoveInfo moveInfo : e) {
                            DefaultItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY);
                        }
                        e.clear();
                        DefaultItemAnimator.this.mMovesList.remove(e);
                    }
                };
                if (b) {
                    ViewCompat.postOnAnimationDelayed(e.get(0).holder.itemView, runnable, ((RecyclerView.ItemAnimator)this).getRemoveDuration());
                }
                else {
                    runnable.run();
                }
            }
            if (b3) {
                final ArrayList<ChangeInfo> e2 = new ArrayList<ChangeInfo>();
                e2.addAll(this.mPendingChanges);
                this.mChangesList.add(e2);
                this.mPendingChanges.clear();
                final Runnable runnable2 = new Runnable() {
                    @Override
                    public void run() {
                        final Iterator<ChangeInfo> iterator = e2.iterator();
                        while (iterator.hasNext()) {
                            DefaultItemAnimator.this.animateChangeImpl((ChangeInfo)iterator.next());
                        }
                        e2.clear();
                        DefaultItemAnimator.this.mChangesList.remove(e2);
                    }
                };
                if (b) {
                    ViewCompat.postOnAnimationDelayed(e2.get(0).oldHolder.itemView, runnable2, ((RecyclerView.ItemAnimator)this).getRemoveDuration());
                }
                else {
                    runnable2.run();
                }
            }
            if (b4) {
                final ArrayList<ViewHolder> e3 = new ArrayList<ViewHolder>();
                e3.addAll(this.mPendingAdditions);
                this.mAdditionsList.add(e3);
                this.mPendingAdditions.clear();
                final Runnable runnable3 = new Runnable() {
                    @Override
                    public void run() {
                        final Iterator<ViewHolder> iterator = e3.iterator();
                        while (iterator.hasNext()) {
                            DefaultItemAnimator.this.animateAddImpl(iterator.next());
                        }
                        e3.clear();
                        DefaultItemAnimator.this.mAdditionsList.remove(e3);
                    }
                };
                if (b || b2 || b3) {
                    long removeDuration;
                    if (b) {
                        removeDuration = ((RecyclerView.ItemAnimator)this).getRemoveDuration();
                    }
                    else {
                        removeDuration = 0L;
                    }
                    long moveDuration;
                    if (b2) {
                        moveDuration = ((RecyclerView.ItemAnimator)this).getMoveDuration();
                    }
                    else {
                        moveDuration = 0L;
                    }
                    long changeDuration;
                    if (b3) {
                        changeDuration = ((RecyclerView.ItemAnimator)this).getChangeDuration();
                    }
                    else {
                        changeDuration = 0L;
                    }
                    ViewCompat.postOnAnimationDelayed(e3.get(0).itemView, runnable3, removeDuration + Math.max(moveDuration, changeDuration));
                }
                else {
                    runnable3.run();
                }
            }
        }
    }
    
    private static class ChangeInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder newHolder;
        public ViewHolder oldHolder;
        public int toX;
        public int toY;
        
        private ChangeInfo(final ViewHolder oldHolder, final ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }
        
        private ChangeInfo(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int fromX, final int fromY, final int toX, final int toY) {
            this(viewHolder, viewHolder2);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
        
        @Override
        public String toString() {
            return "ChangeInfo{oldHolder=" + this.oldHolder + ", newHolder=" + this.newHolder + ", fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + '}';
        }
    }
    
    private static class MoveInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder holder;
        public int toX;
        public int toY;
        
        private MoveInfo(final ViewHolder holder, final int fromX, final int fromY, final int toX, final int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }
    
    private static class VpaListenerAdapter implements ViewPropertyAnimatorListener
    {
        @Override
        public void onAnimationCancel(final View view) {
        }
        
        @Override
        public void onAnimationEnd(final View view) {
        }
        
        @Override
        public void onAnimationStart(final View view) {
        }
    }
}
