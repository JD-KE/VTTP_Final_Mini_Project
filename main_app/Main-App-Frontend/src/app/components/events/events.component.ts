import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { UserStore } from '../../user.store';
import { Subscription, lastValueFrom } from 'rxjs';
import { EventService } from '../../event.service';
import { EventModel, EventResults } from '../../model';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent implements OnInit, OnDestroy {
  
  private userStore = inject(UserStore)
  private eventSvc = inject(EventService)

  isLoggedIn:boolean = false
  userSub!: Subscription
  userEventsP$!:Promise<EventResults>

  resultsLength = 0;
  pageSize:number = 10;
  pageIndex:number = 0;
  pageSizeOptions = [5, 10, 25];
  pageEvent!:PageEvent;
  
  ngOnInit(): void {

    this.userSub = this.userStore.getLoggedinUser.subscribe(
      value => {
        this.isLoggedIn = !!value
        this.userEventsP$ = lastValueFrom(this.eventSvc.getUserEventsPage
          (this.pageIndex+1,this.pageSize))
          .then(value => {
            this.resultsLength = value.totalCount
            return value
          })
      }
    )

  }

  ngOnDestroy(): void {
    this.userSub?.unsubscribe()
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize
    this.userEventsP$ = lastValueFrom(this.eventSvc.getUserEventsPage(this.pageIndex+1,this.pageSize))
      .then(value => {
        this.resultsLength = value.totalCount
        return value
      })
  }

}
