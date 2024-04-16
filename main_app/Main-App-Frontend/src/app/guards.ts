import { inject } from "@angular/core";
import { CanActivateFn, CanDeactivateFn, Router } from "@angular/router";
import { UserStore } from "./user.store";
import { take } from "rxjs";
import { CreateEventComponent } from "./components/create-event/create-event.component";
import { EventGameStore } from "./event-game.store";

export const alreadyAuthenticated: CanActivateFn = 
    (_route, _state)=> {
        const userStore = inject(UserStore)
        const router = inject(Router)
        var userExists 
        userStore.getLoggedinUser.pipe(
            take(1)
        ).subscribe(
            value => userExists = !!value
        )
        if (!userExists) {
            return true
        }
        alert('A user is already logged in. Returning to home.')
        return router.parseUrl('/home')
}

export const isAuthenticated:  CanActivateFn = 
    (_route, _state)=> {
        const userStore = inject(UserStore)
        const router = inject(Router)
        var userExists 
        userStore.getLoggedinUser.pipe(
            take(1)
        ).subscribe(
            value => userExists = !!value
        )
        if (userExists) {
            return true
        }
        alert('Please login to access this feature.')
        return router.parseUrl('/login')
}

export const canLeaveCreateEditEvent: CanDeactivateFn<CreateEventComponent> =
    (comp, _route, _state) => {
        const eventGameStore = inject(EventGameStore)
        const userStore = inject(UserStore)
        var userExists 
        userStore.getLoggedinUser.pipe(
            take(1)
        ).subscribe(
            value => userExists = !!value
        )

        if(comp.hasProcessed)
            return true
        // if(!comp.form.dirty && comp.eventGames.length == 0)
        //     return true
        if(!userExists) {
            eventGameStore.clearEventGames()
            return true
        }
        
        if(!comp.isEdit){
            const leave = confirm('You have not saved your event.\nAre you sure you want to leave?')
            if (leave) {
                eventGameStore.clearEventGames()
                return true
            } else {
                return false
            }
             
        } else {
            
            const leave = confirm('You have not saved your changes.\nAre you sure you want to leave?')
            if (leave) {
                eventGameStore.clearEventGames()
                return true
            } else {
                return false
            }
        }
    
}