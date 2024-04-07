import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserLogin, UserRegister } from '../../model';
import { UserService } from '../../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private fb = inject(FormBuilder)
  private userSvc = inject(UserService)
  private router = inject(Router)

  form!:FormGroup
  loginSub!:Subscription
  
  ngOnInit(): void {
    this.form = this.createForm()
  }

  createForm() {
    return this.fb.group({
      username: this.fb.control('',[Validators.required]),
      password: this.fb.control('',[Validators.required]),
      email: this.fb.control('', [Validators.required, Validators.email])
    })
  }

  processForm() {
    const registerDetails = this.form.value as UserRegister
    this.userSvc.register(registerDetails)
    this.router.navigate(['/'])
  }
}
