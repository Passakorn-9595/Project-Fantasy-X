package project.fantasy.x;

import java.util.Random;

public abstract class Character {
    String name;
    int health;
    int attackPower;
    int armor;
    boolean isDefending;

    public Character(String name, int health, int attackPower, int armor) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.armor = armor;
        this.isDefending = false;
    }

    public abstract int attack();

    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(0, damage - this.armor); // Armor reduces damage, but minimum is 0
        this.health = Math.max(0, this.health - effectiveDamage);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void defend() {
        isDefending = true;
    }
}

// Hero class with specific stats and abilities
class Hero extends Character {
    int skillUses = 3;

    public Hero(String name) {
        super(name, 50, 12, 5);
    }

    @Override
    public int attack() {
        Random random = new Random();
        return random.nextInt(attackPower) + 5;
    }

    public int useSkill() {
        if (skillUses > 0) {
            skillUses--;
            return attack() + 10;
        } else {
            return attack();
        }
    }
}

// Boss class with stronger stats and abilities
class Boss extends Character {

    public Boss(String name) {
        super(name, 80, 8, 4);
    }

    @Override
    public int attack() {
        Random random = new Random();
        return random.nextInt(attackPower) + 3;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
    }
}
