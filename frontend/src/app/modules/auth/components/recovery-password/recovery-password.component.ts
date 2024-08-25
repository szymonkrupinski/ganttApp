import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-recovery-password',
  templateUrl: './recovery-password.component.html',
  styleUrl: './recovery-password.component.css'
})
export class RecoveryPasswordComponent implements OnInit {
  recoveryForm!: FormGroup;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit() {
    this.recoveryForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    })
  }


}
