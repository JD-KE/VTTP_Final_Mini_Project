import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { SearchGamesComponent } from './components/search-games/search-games.component';
import { GameComponent } from './components/game/game.component';
import { EventsComponent } from './components/events/events.component';
import { CreateEventComponent } from './components/create-event/create-event.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { EventComponent } from './components/event/event.component';
import { alreadyAuthenticated, canLeaveCreateEditEvent, isAuthenticated } from './guards';

const routes: Routes = [
  
  {path:"home", component:HomeComponent},
  {path:"game", component:SearchGamesComponent},
  {path:"game/:gameId", component:GameComponent},
  {path:"events", component:EventsComponent},
  {path:"event/edit/:eventId", component:CreateEventComponent, canActivate:[isAuthenticated],
  canDeactivate:[canLeaveCreateEditEvent]},
  {path:"event/create", component:CreateEventComponent, canActivate:[isAuthenticated],
   canDeactivate:[canLeaveCreateEditEvent]},
  {path:"event/:eventId", component:EventComponent},
  
  {path:"login",component:LoginComponent, canActivate:[alreadyAuthenticated]},
  {path:"register",component:RegisterComponent, canActivate:[alreadyAuthenticated]},
  {path:"**", redirectTo:"/home", pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
