import { Component, OnInit, inject } from '@angular/core';
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private userSvc = inject(UserService)

  title = 'frontend';

  ngOnInit(): void {
    this.userSvc.logBackIn()
  }
}
