package io.anuke.novi.entities.combat;

import io.anuke.novi.entities.*;
import io.anuke.novi.entities.base.Player;
import io.anuke.novi.entities.effects.Effects;
import io.anuke.novi.entities.enemies.Base;
import io.anuke.novi.entities.enemies.Enemy;
import io.anuke.novi.items.ProjectileType;
import io.anuke.novi.server.NoviServer;

public class Bullet extends FlyingEntity implements Damager{
	private float life;
	public transient Entity shooter;
	public ProjectileType type = ProjectileType.yellowbullet;

	{
		material.drag = 0;
		material.getRectangle().setSize(2);
		type.setup(this);
	}

	private Bullet(){
		
	}

	public Bullet(float rotation){
		if(NoviServer.active()) initVelocity(rotation);
	}

	public Bullet(ProjectileType type, float rotation){
		this.type = type;
		if(NoviServer.active()) initVelocity(rotation);
	}

	@Override
	public void update(){
		life += delta();
		if(life >= type.getLifetime()){
			remove();
			if(NoviServer.active()) type.destroyEvent(this);
		}
		updateVelocity();
	}

	@Override
	public void draw(){
		type.draw(this);
	}

	public Bullet setShooter(Entity entity){
		shooter = entity;
		return this;
	}

	//sets velocity to speed of projectile type
	private void initVelocity(float rotation){
		velocity.x = 1f;
		velocity.setLength(type.getSpeed());
		velocity.setAngle(rotation);
	}

	//don't want to hit players or other bullets
	public boolean collides(SolidEntity other){
		return type.collide() && super.collides(other) && !((other instanceof Base && !type.collideWithBases()) ||(other instanceof Player && shooter instanceof Player) || (other instanceof Bullet && (!type.collideWithOtherProjectiles() && !((Bullet)other).type.collideWithOtherProjectiles())) || other.equals(shooter) || (shooter instanceof Enemy && other instanceof Enemy));
	}

	@Override
	public void collisionEvent(SolidEntity other){
		Effects.effect(type.hitEffect(), x, y);
		
		removeServer();
		
		if(NoviServer.active()) type.destroyEvent(this);
	}
	
	public float life(){
		return life;
	}

	@Override
	public int damage(){
		return type.damage();
	}

}
