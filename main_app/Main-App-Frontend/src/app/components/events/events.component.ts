import { Component, OnInit, inject } from '@angular/core';
import { UserService } from '../../user.service';
import { UserStore } from '../../user.store';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent implements OnInit {
  
  private userSvc = inject(UserService)
  private userStore = inject(UserStore)

  isLoggedIn:boolean = false
  userSub!: Subscription
  
  ngOnInit(): void {
    this.userSub = this.userStore.getLoggedinUser.subscribe(
      value => {
        this.isLoggedIn = !!value
      }
    )
  }
  

}
