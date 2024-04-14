import { Component, inject } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Observable, Subscription, map } from 'rxjs';
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
      username: this.fb.control('',[Validators.required],[this.usernameValidator()]),
      password: this.fb.control('',[Validators.required, this.passwordValidator()]),
      email: this.fb.control('', [Validators.required, Validators.email], [this.emailValidator()])
    },
    {updateOn:'blur'}
    )
  }

  processForm() {
    const registerDetails = this.form.value as UserRegister
    registerDetails.username = registerDetails.username.trim()
    registerDetails.password = registerDetails.password.trim()
    registerDetails.email = registerDetails.email.trim()
    this.userSvc.register(registerDetails)
  }

  passwordValidator():ValidatorFn {
    return (control:AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }
      const regex = new RegExp('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,64}$');
      return regex.test(control.value) ? null : {invalidPassword:true}
    }
  }

  usernameValidator():AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.userSvc.checkifUsernameExists(control.value).pipe(
        map(value =>{
          return value ? {usernameExists:true} : null
        })
      )
    }
  }

  emailValidator():AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.userSvc.checkifEmailExists(control.value).pipe(
        map(value =>{
          return value ? {emailExists:true} : null
        })
      )
    }
  }
}
