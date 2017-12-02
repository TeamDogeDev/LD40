package de.dogedev.ld40.misc;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.ashley.components.RenderComponent;
import de.dogedev.ld40.assets.enums.Textures;

import static de.dogedev.ld40.Statics.ashley;
import static de.dogedev.ld40.Statics.asset;

public class EntityFactory {

    public static void createPlayer() {}

    public static Entity createEnemy(World world, Vector2 position, float angleRad) {
        Entity entity = ashley.createEntity();

        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.DynamicBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        PolygonShape entityShape = new PolygonShape();
        entityShape.setAsBox(12.8f / 2, 12.8f / 2);

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = 1f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);
        entityShape.dispose();

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.ENEMY);


        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(physicsComponent);

        ashley.addEntity(entity);
        return entity;
    }

    public static void createBase() {}
}
