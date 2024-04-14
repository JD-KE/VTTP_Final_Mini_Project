import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { UserService } from './user.service';
import { Router } from '@angular/router';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  
  private userSvc = inject(UserService)
  private router = inject(Router)

  title = 'frontend';
  checkAuthSub!:Subscription

  ngOnInit(): void {
    this.userSvc.logBackIn()
    this.checkAuthSub = interval(1000*60*30).subscribe(
      value => this.userSvc.logBackIn()
    )
  }

  ngOnDestroy(): void {
    this.checkAuthSub?.unsubscribe()
  }
}
