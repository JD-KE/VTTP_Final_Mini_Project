import { Component, OnInit, inject } from '@angular/core';
import { UserService } from '../../user.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent implements OnInit {
  
  private userSvc = inject(UserService)

  isLoggedIn:boolean = false
  
  ngOnInit(): void {
    this.isLoggedIn = !!this.userSvc.user
  }
  

}
