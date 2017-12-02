package de.dogedev.ld40.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import de.dogedev.ld40.ashley.components.LookComponent;
import de.dogedev.ld40.ashley.components.MovementComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.ashley.components.RenderComponent;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
    public static final ComponentMapper<LookComponent> look = ComponentMapper.getFor(LookComponent.class);
}
