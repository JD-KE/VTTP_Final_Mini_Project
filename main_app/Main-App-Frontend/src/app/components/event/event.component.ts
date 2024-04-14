import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../event.service';
import { Subscription, lastValueFrom, map } from 'rxjs';
import { EventModel } from '../../model';
import { MatDialog } from '@angular/material/dialog';
import { GameComponent } from '../game/game.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrl: './event.component.css'
})
export class EventComponent implements OnInit {
  private router = inject(Router)
  private eventSvc = inject(EventService)
  private activatedRoute = inject(ActivatedRoute)
  private dialog = inject(MatDialog)
  private _snackbar = inject(MatSnackBar)

  eventP$!:Promise<EventModel>
  id!:string
  idSub!:Subscription

  showGames = true
  
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

  toggleGames() {
    this.showGames = !this.showGames
  }

  getGame(id:number) {
    // console.log(id)
    const dialogRef = this.dialog.open(GameComponent, {
      data:id,
    })
  }

  getLink() {
    navigator.clipboard.writeText(window.location.href)
    this._snackbar.open('Link copied', 'Close', {duration: 2000})
    // return window.location.href
  }
}
