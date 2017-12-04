package de.dogedev.ld40.misc;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.dogedev.ld40.ashley.components.*;
import de.dogedev.ld40.assets.enums.Textures;

import static de.dogedev.ld40.Statics.ashley;
import static de.dogedev.ld40.Statics.asset;


public class EntityFactory {

    private static float playerHealth = 1.0f;
    private static float playerDamage = 0.1f;
    private static float enemyHealth = 0.1f;
    private static float enemyDamage = 0.1f;
    private static float bulletHealth = 0.2f;
    private static float bulletDamage = 0.1f;


    static float[] shipVertices = {
            -3.0f, -0.6f,
            -1.5f, -3.3f,
            1.5f, -3.3f,
            3.0f, -0.6f,
            1.5f, 3.3f,
            -1.5f, 3.3f
    };

    static float[] asteroid2Vertices = {
            -3.1f, -1.7f,
            -2.3f, 2.2f,
            2.9f, 2.9f,
            0.0f, -3.0f,
            3.0f, 0.0f,

    };

    private static float[] asteroid1Vertices = {
            -3.0f, 0.0f,
            -2.0f, -2.0f,
            0.0f, -2.0f,
            2.4f, -3.0f,
            3.0f, 2.0f,
            -2.0f, 3.0f
    };

    private static Color[] colors = {
            new Color(0xff000055),
            new Color(0xffff0055),
            new Color(0x0000ff55),
            new Color(0xff00ff55),
            new Color(0xffa50055),
            new Color(0xae00ff55),
            new Color(0x00bfff55),
            new Color(0x8700ff65)
    };

    private static float[] asteroidTexture2Vertices(Textures textures) {
        float[] value = {0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        switch (textures) {
            case ASTEROID_1:
                value = asteroid1Vertices;
                break;
            case ASTEROID_2:
                value = asteroid2Vertices;
                break;
        }
        return value;
    }

    public static Entity createEnemy(World world, Vector2 position, float angleRad, float force, RayHandler rayHandler) {

        boolean b = MathUtils.randomBoolean();

        Textures asteroidTexture = Textures.ASTEROID_1;
        if(b) asteroidTexture = Textures.ASTEROID_2;

        Entity entity = ashley.createEntity();
        BodyDef entityBody = new BodyDef();
        entityBody.type = BodyDef.BodyType.DynamicBody;
        entityBody.position.set(position);
        entityBody.angle = angleRad;

        PolygonShape entityShape = new PolygonShape();
        entityShape.set(asteroidTexture2Vertices(asteroidTexture));

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = 0.8f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);
        physicsComponent.body.applyLinearImpulse(position.nor().scl(force), position.setAngleRad(angleRad + MathUtils.PI / 2), true);
        physicsComponent.body.setAngularVelocity(1);
        entityShape.dispose();

        // Pick random color
        Color c = getRandomColor();


        PointLight pointLight = new PointLight(rayHandler, 30, c, 25, 50, 50);
        pointLight.attachToBody(physicsComponent.body, 0, 0, 90);
        physicsComponent.lights.add(pointLight);

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(asteroidTexture);

        HealthComponent healthComponent = ashley.createComponent(HealthComponent.class);
        healthComponent.health = enemyHealth;
        DamageComponent damageComponent = ashley.createComponent(DamageComponent.class);
        damageComponent.damage = enemyDamage;

        ColorComponent colorComponent = ashley.createComponent(ColorComponent.class);
        colorComponent.color = c;

        entity.add(colorComponent);
        entity.add(damageComponent);
        entity.add(healthComponent);
        entity.add(ashley.createComponent(AsteroidComponent.class));
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

        PointLight pointLight = new PointLight(rayHandler, 25, new Color(0x00ff0055), 50, 50, 50);
        pointLight.attachToBody(physicsComponent.body, 0, 0, 90);
        physicsComponent.lights.add(pointLight);

        ConeLight coneLight = new ConeLight(rayHandler, 128, new Color(0.5f, 0.1f, 1, 1), 120, 50, 50, 0, 20);
        physicsComponent.lights.add(coneLight);

        entityShape.dispose();

        coneLight.attachToBody(physicsComponent.body, 0, 0, 90);

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.PLAYER);

        HealthComponent healthComponent = ashley.createComponent(HealthComponent.class);
        healthComponent.health = playerHealth;

        DamageComponent damageComponent = ashley.createComponent(DamageComponent.class);
        damageComponent.damage = playerDamage;

        entity.add(healthComponent);
        entity.add(damageComponent);

        ColorComponent colorComponent = ashley.createComponent(ColorComponent.class);
        colorComponent.color = new Color(0x00ff0099);

        entity.add(colorComponent);
        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(ashley.createComponent(PlayerComponent.class));
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
        entityShape.setAsBox(0.25f, 0.5f);
//        entityShape.setAsBox(7.1f / 2, 6.4f / 2);

        FixtureDef entityFixture = new FixtureDef();
        entityFixture.shape = entityShape;
        entityFixture.density = 0.4f;

        PhysicsComponent physicsComponent = ashley.createComponent(PhysicsComponent.class);
        physicsComponent.body = world.createBody(entityBody);
        physicsComponent.body.createFixture(entityFixture);
        physicsComponent.body.setUserData(entity);
        physicsComponent.body.applyLinearImpulse(position.nor().scl(force), position.setAngleRad(angleRad + MathUtils.PI / 2), true);
        physicsComponent.body.setFixedRotation(true);
//        physicsComponent.body.setLinearDamping(0.8f);
//        physicsComponent.body.setAngularDamping(2000f);

//        ConeLight coneLight = new ConeLight(rayHandler, 128, new Color(0.5f, 0.1f, 1, 1), 120, 50, 50, 0, 20);

        entityShape.dispose();

        PointLight pointLight = new PointLight(rayHandler, 4, new Color(0xffff0055), 10, 50, 50);
        pointLight.attachToBody(physicsComponent.body, 0, 0, 90);
        physicsComponent.lights.add(pointLight);

        PositionComponent positionComponent = ashley.createComponent(PositionComponent.class);
        RenderComponent renderComponent = ashley.createComponent(RenderComponent.class);
        renderComponent.region = asset.getTextureRegion(Textures.BULLET);

        HealthComponent healthComponent = ashley.createComponent(HealthComponent.class);
        healthComponent.health = bulletHealth;
        DamageComponent damageComponent = ashley.createComponent(DamageComponent.class);
        damageComponent.damage = bulletDamage;

        ColorComponent colorComponent = ashley.createComponent(ColorComponent.class);
        colorComponent.color = Color.YELLOW;

        entity.add(colorComponent);
        entity.add(damageComponent);
        entity.add(healthComponent);
//        entity.add(ashley.createComponent(WeakComponent.class));
        entity.add(ashley.createComponent(BulletComponent.class));
        entity.add(positionComponent);
        entity.add(renderComponent);
        entity.add(physicsComponent);

        ashley.addEntity(entity);
        return entity;
    }


    private static Color getRandomColor() {
        Color color = colors[MathUtils.random(0, colors.length - 1)];
        return color;
    }
}
