import { Component, OnInit, inject } from '@angular/core';
import { UserService } from '../../user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  private userSvc = inject(UserService)

  isLoggedIn = false

  ngOnInit(): void {
    this.userSvc.logBackIn()
    this.isLoggedIn = !!this.userSvc.user
    
  }
  
}
