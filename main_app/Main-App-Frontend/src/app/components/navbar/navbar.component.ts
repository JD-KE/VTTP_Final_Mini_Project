import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { UserService } from '../../user.service';
import { UserStore } from '../../user.store';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy{
  private userSvc = inject(UserService)
  private userStore = inject(UserStore)
  
  isLoggedIn = false
  user!:string
  userSub!: Subscription
  
  ngOnInit(): void {
    // this.userSvc.logBackIn()
    this.userSub = this.userStore.getLoggedinUser.subscribe(
        value => {
          this.user = value
          this.isLoggedIn = !!value
        }
      )
    }

  ngOnDestroy(): void {
    this.userSub.unsubscribe()
  }
    
  logout() {
    this.userSvc.logout()
    this.isLoggedIn = !!this.userSvc.user
    this.user = this.userSvc.user
  }
}
