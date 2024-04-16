import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { UserStore } from '../../user.store';
import { Subscription, interval, lastValueFrom } from 'rxjs';
import { EventService } from '../../event.service';
import { EventModel, EventResults, EventStatus } from '../../model';
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

  currentDate!:Date
  timeInterval!:any
  EventStatus=EventStatus
  
  ngOnInit(): void {
    this.timeInterval = setInterval(() => {
      this.currentDate = new Date()
    }, 1000);

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
    clearInterval(this.timeInterval)
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

  getStatus(event:EventModel):number {
    const startTime = event.startTime
    const endTime = event.endTime
    if (this.currentDate < startTime) {
      return 1
    }
    if (this.currentDate > endTime) {
      return 3
    }
    if(this.currentDate >= startTime && this.currentDate <= endTime) {
      return 2
    }
    return -1
  }

}
