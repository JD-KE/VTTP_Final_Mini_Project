import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserLogin } from '../../model';
import { UserService } from '../../user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  
  private fb = inject(FormBuilder)
  private userSvc = inject(UserService)

  form!:FormGroup
  loginSub!:Subscription
  
  ngOnInit(): void {
    this.form = this.createForm()
  }

  createForm() {
    return this.fb.group({
      username: this.fb.control('',[Validators.required]),
      password: this.fb.control('',[Validators.required])
    })
  }

  processForm() {
    const loginDetails = this.form.value as UserLogin
    this.userSvc.login(loginDetails)
  }
}
