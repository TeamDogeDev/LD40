package de.dogedev.ld40.misc;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.dogedev.ld40.ashley.components.PhysicsComponent;
import de.dogedev.ld40.ashley.components.PositionComponent;
import de.dogedev.ld40.ashley.components.RenderComponent;
import de.dogedev.ld40.assets.enums.Textures;

import static de.dogedev.ld40.Statics.ashley;
import static de.dogedev.ld40.Statics.asset;


public class EntityFactory {

    public static void createPlayer() {}
    static float[] vertices = {
            -3.0f, -0.6f,
            -1.5f, -3.3f,
             1.5f, -3.3f,
             3.0f, -0.6f,
             1.5f,  3.3f,
            -1.5f,  3.3f
    };
    public static void createEnemy() {}

    public static Entity createPlayer(World world, Vector2 position, float angleRad, RayHandler rayHandler) {
        Entity entity = ashley.createEntity();

        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.DynamicBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        PolygonShape entityShape = new PolygonShape();
        entityShape.set(vertices);
//        entityShape.setAsBox(7.1f / 2, 6.4f / 2);

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = 0.4f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);
        physicsComponent.body.setLinearDamping(0.8f);
        physicsComponent.body.setAngularDamping(2f);

        ConeLight coneLight = new ConeLight(rayHandler, 128, new Color(0.5f, 0.1f, 1, 1), 120, 50, 50, 0, 20);
        physicsComponent.coneLight = coneLight;

        entityShape.dispose();

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.PLAYER);


        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(physicsComponent);

        ashley.addEntity(entity);
        return entity;
    }

    public static Entity createBase(World world, Vector2 position, float angleRad) {
        Entity entity = ashley.createEntity();

        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.StaticBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        CircleShape entityShape = new CircleShape();
        entityShape.setRadius(10f / 2);

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = .1f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);

        entityShape.dispose();

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.BASE);


        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(physicsComponent);

        ashley.addEntity(entity);
        return entity;
    }
}
