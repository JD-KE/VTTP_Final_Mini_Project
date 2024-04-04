import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { User } from "./model";

const INIT_STATE: User = {
    username:''
}

@Injectable()
export class UserStore extends ComponentStore<User> {
    constructor() {
        super(INIT_STATE)
    }

    readonly addUser = this.updater<string>(
        (slice:User, username:string) => {
            const newSlice = {
                username: username
            } as User
            
            return newSlice
        }
    )

    readonly removeUser = this.updater<void>(
        (slice:User) => {
            return {
                username:''
            } as User
        }
    )

    readonly getLoggedinUser = this.select(
        (slice:User) => slice.username
    )
}