package common;

public final class Constants {
  /* Here we store any constant values that will be used in the project*/
  private Constants() {
  }

  public static final int LEVEL_1_XP = 250;
  public static final int LEVELUP_XP = 50;
  public static final int DEFAULT_KILL_XP = 200;
  public static final int KILL_XP_PER_LEVEL = 40;

  public static final int PYRO_HP = 500;
  public static final float PYRO_VOLCANIC_BONUS = 0.25f;
  public static final int PYRO_LEVELUP_HP = 50;
  public static final int FIREBLAST_DAMAGE = 350;
  public static final int FIREBLAST_LEVELUP_DAMAGE = 50;
  public static final float FIREBLAST_MOD_ROGUE = 1 - 0.2f;
  public static final float FIREBLAST_MOD_KNIGHT = 1 + 0.2f;
  public static final float FIREBLAST_MOD_PYRO = 1 - 0.1f;
  public static final float FIREBLAST_MOD_WIZARD = 1 + 0.05f;

  public static final int IGNITE_DAMAGE = 150;
  public static final int IGNITE_OVERTIME = 2;
  public static final int IGNITE_LEVELUP_DAMAGE = 20;
  public static final int IGNITE_OVERTIME_DAMAGE = 50;
  public static final int IGNITE_LEVELUP_OVERTIME = 50;
  public static final float IGNITE_MOD_ROGUE = 1 - 0.2f;
  public static final float IGNITE_MOD_KNIGHT = 1 + 0.2f;
  public static final float IGNITE_MOD_PYRO = 1 - 0.1f;
  public static final float IGNITE_MOD_WIZARD = 1 + 0.05f;

  public static final int KNIGHT_HP = 900;
  public static final float KNIGHT_LAND_BONUS = 0.15f;
  public static final int KNIGHT_LEVELUP_HP = 80;
  public static final int EXECUTE_DAMAGE = 200;
  public static final int EXECUTE_LEVELUP_DAMAGE = 30;
  public static final float EXECUTE_HP_PERCENTAGE_LIMIT = 0.2f;
  public static final float EXECUTE_LEVELUP_PERCENTAGE = 0.01f;
  public static final float EXECUTE_MAX_PERCENTAGE = 0.4f;

  public static final float EXECUTE_MOD_ROGUE = 1 + 0.15f;
  public static final float EXECUTE_MOD_KNIGHT = 1 + 0f;
  public static final float EXECUTE_MOD_PYRO = 1 + 0.1f;
  public static final float EXECUTE_MOD_WIZARD = 1 - 0.2f;

  public static final int SLAM_DAMAGE = 100;
  public static final int SLAM_LEVELUP_DAMAGE = 40;
  public static final float SLAM_MOD_ROGUE = 1 - 0.2f;
  public static final float SLAM_MOD_KNIGHT = 1 + 0.2f;
  public static final float SLAM_MOD_PYRO = 1 - 0.1f;
  public static final float SLAM_MOD_WIZARD = 1 + 0.05f;

  public static final int WIZARD_HP = 400;
  public static final float WIZARD_DESERT_BONUS = 0.1f;
  public static final int WIZARD_LEVELUP_HP = 30;
  public static final float DRAIN_PERCENTAGE = 0.2f;
  public static final float DRAIN_LEVELUP_PERCENTAGE = 0.05f;
  public static final float DRAIN_BASE_HP = 0.3f;
  public static final float DRAIN_MOD_ROGUE = 1 - 0.2f;
  public static final float DRAIN_MOD_KNIGHT = 1 + 0.2f;
  public static final float DRAIN_MOD_PYRO = 1 - 0.1f;
  public static final float DRAIN_MOD_WIZARD = 1 + 0.05f;

  public static final float DEFLECT_PERCENTAGE = 0.35f;
  public static final float DEFLECT_LEVELUP_PERCENTAGE = 0.02f;
  public static final float DEFLECT_UPPER_LIMIT = 0.7f;
  public static final float DEFLECT_MOD_ROGUE = 1 + 0.2f;
  public static final float DEFLECT_MOD_KNIGHT = 1 + 0.4f;
  public static final float DEFLECT_MOD_PYRO = 1 + 0.3f;
  public static final float DEFLECT_MOD_WIZARD = 0f;

  public static final int ROGUE_HP = 600;
  public static final float ROGUE_WOODS_BONUS = 0.15f;

  public static final int ROGUE_LEVELUP_HP = 40;
  public static final int BACKSTAB_DAMAGE = 200;
  public static final int BACKSTAB_LEVELUP_DAMAGE = 20;
  public static final float BACKSTAB_MOD_ROGUE = 1 + 0.2f;
  public static final float BACKSTAB_MOD_KNIGHT = 1 - 0.1f;
  public static final float BACKSTAB_MOD_PYRO = 1 + 0.25f;
  public static final float BACKSTAB_MOD_WIZARD = 1 + 0.25f;

  public static final int PARALYSIS_DAMAGE = 40;
  public static final int PARALYSIS_LEVELUP_DAMAGE = 10;
  public static final int PARALYSIS_OVERTIME_ROUNDS = 3;
  public static final int PARALYSIS_OVERTIME_ROUNDS_WOODS = 6;

  public static final float PARALYSIS_MOD_ROGUE = 1f - 0.1f;
  public static final float PARALYSIS_MOD_KNIGHT = 1f - 0.2f;
  public static final float PARALYSIS_MOD_PYRO = 1f + 0.2f;
  public static final float PARALYSIS_MOD_WIZARD = 1f + 0.25f;

  public static final float DAMAGE_ANGEL_KNIGHT_MOD = 0.15f;
  public static final float DAMAGE_ANGEL_PYRO_MOD = 0.2f;
  public static final float DAMAGE_ANGEL_ROGUE_MOD = 0.3f;
  public static final float DAMAGE_ANGEL_WIZARD_MOD = 0.4f;

  public static final int DARK_ANGEL_KNIGHT_HP_BONUS = -40;
  public static final int DARK_ANGEL_PYRO_HP_BONUS = -30;
  public static final int DARK_ANGEL_ROGUE_HP_BONUS = -10;
  public static final int DARK_ANGEL_WIZARD_HP_BONUS = -20;

  public static final float DRACULA_KNIGHT_MOD = -0.2f;
  public static final float DRACULA_PYRO_MOD = -0.3f;
  public static final float DRACULA_ROGUE_MOD = -0.1f;
  public static final float DRACULA_WIZARD_MOD = -0.4f;
  public static final int DRACULA_KNIGHT_HP_BONUS = -60;
  public static final int DRACULA_PYRO_HP_BONUS = -40;
  public static final int DRACULA_ROGUE_HP_BONUS = -35;
  public static final int DRACULA_WIZARD_HP_BONUS = -20;

  public static final float GOODBOY_KNIGHT_MOD = 0.4f;
  public static final float GOODBOY_PYRO_MOD = 0.5f;
  public static final float GOODBOY_ROGUE_MOD = 0.4f;
  public static final float GOODBOY_WIZARD_MOD = 0.3f;
  public static final int GOODBOY_KNIGHT_HP_BONUS = 20;
  public static final int GOODBOY_PYRO_HP_BONUS = 30;
  public static final int GOODBOY_ROGUE_HP_BONUS = 40;
  public static final int GOODBOY_WIZARD_HP_BONUS = 50;

  public static final float LEVELUP_ANGEL_KNIGHT_MOD = 0.1f;
  public static final float LEVELUP_ANGEL_PYRO_MOD = 0.2f;
  public static final float LEVELUP_ANGEL_ROGUE_MOD = 0.15f;
  public static final float LEVELUP_ANGEL_WIZARD_MOD = 0.25f;

  public static final int LIFEGIVER_KNIGHT_HP_BONUS = 100;
  public static final int LIFEGIVER_PYRO_HP_BONUS = 80;
  public static final int LIFEGIVER_ROGUE_HP_BONUS = 90;
  public static final int LIFEGIVER_WIZARD_HP_BONUS = 120;

  public static final float SMALL_ANGEL_KNIGHT_MOD = 0.1f;
  public static final float SMALL_ANGEL_PYRO_MOD = 0.15f;
  public static final float SMALL_ANGEL_ROGUE_MOD = 0.05f;
  public static final float SMALL_ANGEL_WIZARD_MOD = 0.1f;
  public static final int SMALL_ANGEL_KNIGHT_HP_BONUS = 10;
  public static final int SMALL_ANGEL_PYRO_HP_BONUS = 15;
  public static final int SMALL_ANGEL_ROGUE_HP_BONUS = 20;
  public static final int SMALL_ANGEL_WIZARD_HP_BONUS = 25;

  public static final int SPAWNER_KNIGHT_HP = 200;
  public static final int SPAWNER_PYRO_HP = 150;
  public static final int SPAWNER_ROGUE_HP = 180;
  public static final int SPAWNER_WIZARD_HP = 120;

  public static final int XP_ANGEL_KNIGHT_HP_BONUS = 45;
  public static final int XP_ANGEL_PYRO_HP_BONUS = 50;
  public static final int XP_ANGEL_ROGUE_HP_BONUS = 40;
  public static final int XP_ANGEL_WIZARD_HP_BONUS = 60;

  public static final float KNIGHT_STRATEGY_HP_UPPER_LIMIT = 1 / 2f;
  public static final float KNIGHT_STRATEGY_HP_LOWER_LIMIT = 1 / 3f;
  public static final float KNIGHT_HP_BONUS_ATTACKING = -1 / 5f;
  public static final float KNIGHT_MOD_BONUS_ATTACKING = 0.5f;
  public static final float KNIGHT_HP_BONUS_DEFENSIVE = 1 / 4f;
  public static final float KNIGHT_MOD_BONUS_DEFENSIVE = -0.2f;

  public static final float PYRO_STRATEGY_HP_UPPER_LIMIT = 1 / 3f;
  public static final float PYRO_STRATEGY_HP_LOWER_LIMIT = 1 / 4f;
  public static final float PYRO_HP_BONUS_ATTACKING = -1 / 4f;
  public static final float PYRO_MOD_BONUS_ATTACKING = 0.7f;
  public static final float PYRO_HP_BONUS_DEFENSIVE = 1 / 3f;
  public static final float PYRO_MOD_BONUS_DEFENSIVE = -0.3f;

  public static final float ROGUE_STRATEGY_HP_UPPER_LIMIT = 1 / 5f;
  public static final float ROGUE_STRATEGY_HP_LOWER_LIMIT = 1 / 7f;
  public static final float ROGUE_HP_BONUS_ATTACKING = -1 / 7f;
  public static final float ROGUE_MOD_BONUS_ATTACKING = 0.4f;
  public static final float ROGUE_HP_BONUS_DEFENSIVE = 1 / 2f;
  public static final float ROGUE_MOD_BONUS_DEFENSIVE = -0.1f;

  public static final float WIZARD_STRATEGY_HP_UPPER_LIMIT = 1f / 2f;
  public static final float WIZARD_STRATEGY_HP_LOWER_LIMIT = 1f / 4f;
  public static final float WIZARD_HP_BONUS_ATTACKING = -1f / 10f;
  public static final float WIZARD_MOD_BONUS_ATTACKING = 0.6f;
  public static final float WIZARD_HP_BONUS_DEFENSIVE = 1f / 5f;
  public static final float WIZARD_MOD_BONUS_DEFENSIVE = -0.2f;
}
