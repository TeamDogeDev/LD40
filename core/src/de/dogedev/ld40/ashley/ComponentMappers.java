package de.dogedev.ld40.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import de.dogedev.ld40.ashley.components.*;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
    public static final ComponentMapper<LookComponent> look = ComponentMapper.getFor(LookComponent.class);
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<WeakComponent> weak = ComponentMapper.getFor(WeakComponent.class);
    public static final ComponentMapper<BulletComponent> bullet = ComponentMapper.getFor(BulletComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<AsteroidComponent> asteroid = ComponentMapper.getFor(AsteroidComponent.class);
    public static final ComponentMapper<ColorComponent> color = ComponentMapper.getFor(ColorComponent.class);
}
