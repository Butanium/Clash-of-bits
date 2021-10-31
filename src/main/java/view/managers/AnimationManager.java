package view.managers;

import view.fx.AnimationType;
import view.fx.GraphicModuleAnimation;

import java.util.*;

public class AnimationManager {
    private final ViewManager viewManager;
    private HashMap<Integer, Set<GraphicModuleAnimation>> availableAnimTable = new HashMap<>();
    private HashMap<Integer, Set<GraphicModuleAnimation>> usedAnimaTable = new HashMap<>();

    public AnimationManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void createAnimation(AnimationType animType, int x, int y, int z, double scale) {
        int animId = animType.getAnimId();
        Set<GraphicModuleAnimation> availableAnim = availableAnimTable.getOrDefault(animId,
                new HashSet<>());
        if (availableAnim.isEmpty()) {
            Optional<GraphicModuleAnimation> opt = getAvailableAnim();
            if (opt.isPresent()) {
                GraphicModuleAnimation anim = opt.get();
                anim.reInit(x, y, z, scale, animType);
                addUsed(animId, anim);
            } else {
                addUsed(animId, new GraphicModuleAnimation(viewManager, animType, x, y, z, scale));
            }
        } else {
            GraphicModuleAnimation anim = availableAnim.stream().findFirst().get();
            anim.reset(x, y, scale);
            availableAnim.remove(anim);
            addUsed(animId, anim);
        }
    }

    private Optional<GraphicModuleAnimation> getAvailableAnim() {
        for (Set<GraphicModuleAnimation> set : availableAnimTable.values()) {
            if (!set.isEmpty()) {
                GraphicModuleAnimation anim = set.stream().findAny().get();
                set.remove(anim);
                return Optional.of(anim);
            }
        }
        return Optional.empty();
    }

    private void addToSet(int animId, GraphicModuleAnimation anim, HashMap<Integer, Set<GraphicModuleAnimation>> table) {
        Set<GraphicModuleAnimation> animSet = table.getOrDefault(animId,
                new HashSet<>());
        animSet.add(anim);
        table.put(animId, animSet);
    }

    private void addUsed(int animId, GraphicModuleAnimation anim) {
        addToSet(animId, anim, usedAnimaTable);
    }

    private void addAvailable(int animId, GraphicModuleAnimation anim) {
        addToSet(animId, anim, availableAnimTable);
    }

    public void updateAnimations() {
        Set<Map.Entry<Integer, Set<GraphicModuleAnimation>>> entrySet = usedAnimaTable.entrySet();
        for (Map.Entry<Integer, Set<GraphicModuleAnimation>> anims : usedAnimaTable.entrySet()) {
            HashSet<GraphicModuleAnimation> toRemove = new HashSet<>();
            for (GraphicModuleAnimation anim : anims.getValue()) {
                anim.update();
                if (!anim.isActive()) {
                    anim.onRemove();
                    anim.updateVisibility();
                    toRemove.add(anim);
                    addAvailable(anims.getKey(), anim);
                }
            }
            anims.getValue().removeAll(toRemove);
        }
    }
}
