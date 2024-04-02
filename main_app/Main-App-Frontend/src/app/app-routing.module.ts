import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SearchGamesComponent } from './components/search-games/search-games.component';
import { GameComponent } from './components/game/game.component';
import { EventsComponent } from './components/events/events.component';
import { CreateEventComponent } from './components/create-event/create-event.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  {path:"", component:HomeComponent},
  {path:"home", component:HomeComponent},
  {path:"game", component:SearchGamesComponent},
  {path:"game/:gameId", component:GameComponent},
  {path:"event", component:EventsComponent},
  {path:"event/create", component:CreateEventComponent},
  {path:"login",component:LoginComponent},
  {path:"register",component:RegisterComponent},
  {path:"**", redirectTo:"/", pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
