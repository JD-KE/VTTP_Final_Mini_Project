import { Component, OnInit, inject } from '@angular/core';
import { UserStore } from '../../user.store';
import { Subscription, lastValueFrom } from 'rxjs';
import { EventService } from '../../event.service';
import { EventModel } from '../../model';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent implements OnInit {
  
  private userStore = inject(UserStore)
  private eventSvc = inject(EventService)

  isLoggedIn:boolean = false
  userSub!: Subscription
  userEventsP$!:Promise<EventModel[]>
  
  ngOnInit(): void {

    this.userSub = this.userStore.getLoggedinUser.subscribe(
      value => {
        this.isLoggedIn = !!value
      }
    )

    this.userEventsP$ = lastValueFrom(this.eventSvc.getUserEvents())
  }
  

}
