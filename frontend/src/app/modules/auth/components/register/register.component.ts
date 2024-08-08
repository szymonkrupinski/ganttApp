import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {AppState} from "../../../../store/app.reducer";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  passwordFieldType: string = 'password';
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder, private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]]
      },
      {validators: this.passwordsMatch});
  }

  passwordsMatch(group: FormGroup) {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : {notMatching: true};
  }

  togglePasswordVisibility() {
    if (this.passwordFieldType === 'password') {
      this.passwordFieldType = 'text';
    } else {
      this.passwordFieldType = 'password';
    }
  }

}
