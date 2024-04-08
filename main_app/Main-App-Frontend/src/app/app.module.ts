import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { SearchGamesComponent } from './components/search-games/search-games.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from './material-module/material.module';
import { GameService } from './game.service';
import { GameComponent } from './components/game/game.component';

import { CreateEventComponent } from './components/create-event/create-event.component';
import { EventsComponent } from './components/events/events.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { EventGameStore } from './event-game.store';
import { UserService } from './user.service';
import { LoginComponent } from './components/login/login.component';
import { JwtModule } from '@auth0/angular-jwt';
import { RegisterComponent } from './components/register/register.component';
import { UserStore } from './user.store';
import { EventComponent } from './components/event/event.component';
import { DatePipe } from '@angular/common';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    SearchGamesComponent,
    GameComponent,
    CreateEventComponent,
    EventsComponent,
    LoginComponent,
    RegisterComponent,
    EventComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    JwtModule.forRoot({
      config:{
        tokenGetter: (request) => {
          if(request?.url.includes('refresh-token')) {
            return localStorage.getItem('refreshToken')
          }
          return localStorage.getItem('accessToken')
        },
        allowedDomains:['localhost:8080','caring-sheep-production.up.railway.app'],
        disallowedRoutes:[
          /.*\/api\/auth\/(authentication|register)/,
          /.*\/api\/game\/.*/
        ]
      }
    })
  ],
  providers: [
    provideAnimationsAsync(),
    GameService,
    EventGameStore,
    UserService,
    UserStore,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
