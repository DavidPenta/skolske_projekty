from django.urls import path, include
from myproject import views
import myproject.views.v1
import myproject.views.v2
import myproject.views.v3
import myproject.views.v4

urlpatterns = {
    path('', myproject.views.v1.getData_V1),
    path('v1/health/', views.v1.getData_V1),
    path('v2/patches/', views.v2.getData_V2_patches),
    path('v2/players/<int:player_id>/game_exp/', views.v2.getData_V2_game_exp),
    path('v2/players/<int:player_id>/game_objectives/', views.v2.getData_V2_game_objectives),
    path('v2/players/<int:player_id>/abilities/', views.v2.getData_V2_abilities),
    path('v3/matches/<int:match_id>/top_purchases/', views.v3.getData_v3_top_purchases),
    path('v3/abilities/<int:ability_id>/usage/', views.v3.getData_v3_usage),
    path('v3/statistics/tower_kills/', views.v3.getData_v3_tower_kills),
    path('v4/patches/', views.v4.getData_V4_patches),
    path('v4/players/<int:player_id>/game_exp/', views.v4.getData_V4_game_exp),
    path('v4/players/<int:player_id>/game_objectives/', views.v4.getData_V4_game_objectives),
    path('v4/players/<int:player_id>/abilities/', views.v4.getData_V4_abilities),
    path('v4/matches/<int:match_id>/top_purchases/', views.v4.getData_v4_top_purchases),
    path('v4/abilities/<int:ability_id>/usage/', views.v4.getData_v4_usage),
    path('v4/statistics/tower_kills/', views.v4.getData_v4_tower_kills)
}
