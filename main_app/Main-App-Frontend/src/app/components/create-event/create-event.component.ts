import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { SearchGamesComponent } from '../search-games/search-games.component';
import { EventGameStore } from '../../event-game.store';
import { Observable, Subscription, lastValueFrom } from 'rxjs';
import { EventModel, GameSummary } from '../../model';
import { EventService } from '../../event.service';
import { GameComponent } from '../game/game.component';
import { UserStore } from '../../user.store';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { LoadingOverlayRef, LoadingService } from '../../loading.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.css'
})
export class CreateEventComponent implements OnInit, OnDestroy{

  private fb = inject(FormBuilder)
  private dialog = inject(MatDialog)
  private eventGamesStore = inject(EventGameStore)
  private eventSvc = inject(EventService)
  private userStore = inject(UserStore)
  private router = inject(Router)
  private activatedRoute = inject(ActivatedRoute)
  private datePipe = inject(DatePipe)
  private loadingSvc = inject(LoadingService)
  
  
  form!:FormGroup
  user!:string
  userSub!:Subscription
  eventGames!:GameSummary[]
  eventGamesSub!:Subscription
  eventGames$!: Observable<GameSummary[]>
  hasEventGameSub!:Subscription
  hasGame!:boolean
  createEventSub!:Subscription
  hasProcessed = false
  showGames = true
  loadingRef!:LoadingOverlayRef

  id!:string
  event!:EventModel
  isEdit:boolean = false
  updateEventSub!:Subscription
  
  ngOnInit(): void {
    this.isEdit = this.router.url.includes('edit')

    this.form = this.createForm();
    this.eventGames$ = this.eventGamesStore.getEventGames
    this.eventGamesSub = this.eventGames$.subscribe(
      eventGames => {
        this.eventGames = eventGames
      }
    )

    this.hasEventGameSub = this.eventGamesStore.hasEventGames.subscribe(
      hasGame => this.hasGame = hasGame
    )

    this.userSub = this.userStore.getLoggedinUser.subscribe(
      value => {
        this.user = value
      }
    )

    if (this.isEdit) {
      this.id = this.activatedRoute.snapshot.params['eventId']
      lastValueFrom(this.eventSvc.getEventById(this.id))
        .then(value =>{
          if(value.userCreated !== this.user) {
            alert("Not the user who created this event, returning to events")
            this.router.navigate(['/events'])
          }
          return value
        })
        .then(value => {

          this.form.patchValue(value)
          this.form.controls['startTime'].patchValue(this.datePipe.transform(value.startTime, 'yyyy-MM-ddTHH:mm'))
          this.form.controls['endTime'].patchValue(this.datePipe.transform(value.endTime, 'yyyy-MM-ddTHH:mm'))
          this.eventGamesStore.addAllEventGamesFromExistingEvent(value.games)
        })
      
    }
    
  }

  ngOnDestroy(): void {
    this.hasEventGameSub.unsubscribe()
    this.createEventSub?.unsubscribe()
    this.updateEventSub?.unsubscribe()
  }
  
  getGame(id:number) {
    // console.log(id)
    const dialogRef = this.dialog.open(GameComponent, {
      data:id,
    })
  }
    
  openSearch() {
    const dialogRef = this.dialog.open(SearchGamesComponent, {data:true})
  }

  deleteGameFromStore(id:number) {
    // console.log('delete')
    this.eventGamesStore.removeGameFromEvent(id)
  }

  toggleGames() {
    this.showGames = !this.showGames
   }

  processBooking() {
    this.loadingRef = this.loadingSvc.open()
    const event:EventModel = {
      ...this.form.value,
      startTime: new Date(this.form.get('startTime')?.value).getTime(),
      endTime: new Date(this.form.get('endTime')?.value).getTime(),
      userCreated:this.user,
      games:this.eventGames
    } as EventModel
    event.name = event.name.trim()
    event.description = event.description.trim()
    event.details = event.details.trim()
    // console.log(event)

    if(!this.isEdit){
      this.createEventSub = this.eventSvc.createEvent(event).subscribe({
        next: value => {
          // console.log(value)
          this.loadingRef.close()
          alert('Event created')
          const eventId:string = value.eventId
          this.eventGamesStore.clearEventGames()
          this.hasProcessed = true
          this.router.navigate(['/event',eventId])
        },
        error: error => {
          this.loadingRef.close()
          alert(error.error.message)
        },
        complete: () => this.createEventSub.unsubscribe()
      })
    } else {

      event.id = this.id

      this.updateEventSub = this.eventSvc.updateEvent(event).subscribe({
        next: value => {
          // console.log(value)
          this.loadingRef.close()
          alert('Event updated')
          this.eventGamesStore.clearEventGames()
          this.hasProcessed = true
          this.router.navigate(['/event',this.id])
        },
        error: error => {
          this.loadingRef.close()
          alert(error.error.message)
        },
        complete: () => this.updateEventSub.unsubscribe()
      })
    }
    
    
  }

  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required,]),
      description: this.fb.control<string>('', [Validators.required,]),
      startTime: this.fb.control<string>('', [Validators.required] ),
      endTime: this.fb.control<string>('', [Validators.required,]),
      details: this.fb.control<string>('')
    },
    {validators:endLaterThanStart})
  }

  
}

export function startDateLaterThanNow(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const controlValue = control.value;
    const currentDateTime = new Date();
    return (currentDateTime > new Date(controlValue)) ? { laterThanCurrentDateTime: 
      { value: true} } : null;
  };
}

export const endLaterThanStart: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const startTime = control.get('startTime')?.value;
  const endTime = control.get('endTime')?.value;

  if(!!startTime && !!endTime) {
    const endLaterThanStart = (new Date(startTime).getTime() < new Date(endTime).getTime())
    // for mat-error in endTime
    if(!endLaterThanStart) {
      control.get('endTime')?.setErrors({endLaterThanStart: {value: false}})
    } else {
      control.get('endTime')?.setErrors(null)
    }
    return endLaterThanStart ? null : {endLaterThanStart: {value: false}}
  }

  return {startEndTimesEntered: {value:false}}
};