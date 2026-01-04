package hust.project.freshfridge.domain.constant;

public enum ActionType {
    // Auth actions
    USER_REGISTER,
    USER_LOGIN,
    USER_LOGOUT,
    PASSWORD_CHANGE,
    
    // Group actions
    GROUP_CREATE,
    GROUP_JOIN,
    GROUP_LEAVE,
    GROUP_MEMBER_ADDED,
    GROUP_MEMBER_REMOVED,
    
    // Kitchen actions
    KITCHEN_ITEM_ADDED,
    KITCHEN_ITEM_UPDATED,
    KITCHEN_ITEM_REMOVED,
    KITCHEN_ITEM_CONSUMED,
    
    // Shopping actions
    SHOPPING_LIST_CREATED,
    SHOPPING_TASK_ADDED,
    SHOPPING_TASK_PURCHASED,
    
    // Meal actions
    MEAL_PLANNED,
    MEAL_UPDATED,
    MEAL_DELETED,
    
    // Recipe actions
    RECIPE_CREATED,
    RECIPE_UPDATED,
    RECIPE_DELETED
}
