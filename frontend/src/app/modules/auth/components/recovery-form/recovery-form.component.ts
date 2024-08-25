import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-recovery-form',
  templateUrl: './recovery-form.component.html',
  styleUrl: './recovery-form.component.css'
})
export class RecoveryFormComponent implements OnInit {
  passwordFieldType: string = 'password';
  recoveryForm!: FormGroup;


  constructor(private fb: FormBuilder, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.recoveryForm = this.fb.group({
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]]
      },
      {validators: this.passwordsMatch});

    this.route.paramMap.subscribe({
      next: (param) => {
        console.log(param.get("uid"));
      }
    })


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
