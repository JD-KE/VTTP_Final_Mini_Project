import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../event.service';
import { Subscription, lastValueFrom, map } from 'rxjs';
import { EventModel } from '../../model';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrl: './event.component.css'
})
export class EventComponent implements OnInit {
  private router = inject(Router)
  private eventSvc = inject(EventService)
  private activatedRoute = inject(ActivatedRoute)

  eventP$!:Promise<EventModel>
  id!:string
  idSub!:Subscription
  
  ngOnInit(): void {
    this.idSub = this.activatedRoute.params.pipe(
      map(params => params['eventId'])
    ).subscribe(
      value => {
        // console.log(value)
        this.id = value
        this.eventP$ = lastValueFrom(this.eventSvc.getEventById(this.id))
    })
  }
}
