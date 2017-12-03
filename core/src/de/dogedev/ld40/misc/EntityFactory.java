package de.dogedev.ld40.misc;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.dogedev.ld40.ashley.components.*;
import de.dogedev.ld40.assets.enums.Textures;

import static de.dogedev.ld40.Statics.ashley;
import static de.dogedev.ld40.Statics.asset;


public class EntityFactory {

    public static void createPlayer() {}
    static float[] shipVertices = {
            -3.0f, -0.6f,
            -1.5f, -3.3f,
             1.5f, -3.3f,
             3.0f, -0.6f,
             1.5f,  3.3f,
            -1.5f,  3.3f
    };

    static float[] asteroid2Vertices = {
            -3.1f, -1.7f,
            -2.3f, 2.2f,
            2.9f, 2.9f,
            0.0f, -3.0f,
            3.0f, 0.0f,

    };



    public static Entity createEnemy(World world, Vector2 position, float angleRad, float force) {
        Entity entity = ashley.createEntity();
        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.DynamicBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        PolygonShape entityShape = new PolygonShape();
        entityShape.set(asteroid2Vertices);
//        entityShape.setAsBox(7.1f / 2, 6.4f / 2);

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = 0.8f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);
        physicsComponent.body.applyLinearImpulse(position.nor().scl(force), position.setAngleRad(angleRad + MathUtils.PI/2), true);
//        physicsComponent.body.setAngularDamping(30000);
//        physicsComponent.body.setLinearDamping(30000);
        physicsComponent.body.setAngularVelocity(1);
        entityShape.dispose();

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.ASTEROID_2);

        HealthComponent healthComponent = ashley.createComponent(HealthComponent.class);
        DamageComponent damageComponent = ashley.createComponent(DamageComponent.class);
        damageComponent.damage = 1.0f;

        entity.add(damageComponent);
        entity.add(healthComponent);
        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(physicsComponent);

        ashley.addEntity(entity);
        return entity;
    }

    public static Entity createPlayer(World world, Vector2 position, float angleRad, RayHandler rayHandler) {
        Entity entity = ashley.createEntity();

        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.DynamicBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        PolygonShape entityShape = new PolygonShape();
        entityShape.set(shipVertices);
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

        coneLight.attachToBody(physicsComponent.body, 0,0, 90);

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.PLAYER);

//        HealthComponent healthComponent = ashley.createComponent(HealthComponent.class);

//        entity.add(healthComponent);

        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(physicsComponent);

        ashley.addEntity(entity);
        return entity;
    }

    public static Entity createBullet(World world, Vector2 position, float angleRad, float force, RayHandler rayHandler) {
        Entity entity = ashley.createEntity();

        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.DynamicBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        PolygonShape entityShape = new PolygonShape();
        entityShape.setAsBox(0.25f,0.5f);
//        entityShape.setAsBox(7.1f / 2, 6.4f / 2);

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = 0.4f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);
        physicsComponent.body.applyLinearImpulse(position.nor().scl(force), position.setAngleRad(angleRad + MathUtils.PI/2), true);
        physicsComponent.body.setFixedRotation(true);
//        physicsComponent.body.setLinearDamping(0.8f);
//        physicsComponent.body.setAngularDamping(2000f);

//        ConeLight coneLight = new ConeLight(rayHandler, 128, new Color(0.5f, 0.1f, 1, 1), 120, 50, 50, 0, 20);
//        physicsComponent.coneLight = coneLight;

        entityShape.dispose();

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.BULLET);

        HealthComponent healthComponent = ashley.createComponent(HealthComponent.class);
        DamageComponent damageComponent = ashley.createComponent(DamageComponent.class);
        damageComponent.damage = 1.0f;

        entity.add(damageComponent);
        entity.add(healthComponent);
        entity.add(ashley.createComponent(WeakComponent.class));
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
