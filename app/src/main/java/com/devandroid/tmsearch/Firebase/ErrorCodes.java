package com.devandroid.tmsearch.Firebase;


public final class ErrorCodes {

    /**
     * Firebase Auth Errors
     */
    public static String FirebaseAuthInvalidCredentialsException = "Não foi possível conectar ao servidor com este email ou senha, por favor verifique suas credenciais e tente novamente.";
    public static String FirebaseAuthInvalidUserException = "Endereço de email não cadastrado, por favor verifique seu email e tente novamente. ";
    public static String FirebaseAuthGenericError = "Erro ao logar usuário.";
    public static String FirebaseAuthReauthenticateGenericError = "Erro ao autenticar usuário, por favor verifique sua conexão com a internet e tente novamente.";
    public static String FirebaseAuthWeakPasswordException = "A senha deve conter ao menos 6 caracteres, por favor tente novamente.";
    public static String FirebaseAuthInvalidCredentialsExceptionCreateUsers = "Endereço de email mal formatado, por favor verifique seu email e tente novamente.";
    public static String FirebaseAuthUserCollisionException = "Endereço de email já existente, por favor digite outro email.";
    public static String FirebaseAuthGenericErrorCreateUser = "Erro ao criar usuário.";
    public static String FirebaseNetworkException = "Erro ao conectar com servidor, por favor verifique sua conexão com a internet e tente novamente.";
    public static String FirebaseAuthChangeEmailError = "Não foi possível alterar o email.";
    public static String FirebaseAuthChangePasswordError = "Não foi possível alterar a senha.";
    public static String FirebaseDbSetApiKeyError = "Não foi possível persistir a chave da API.";

    /**
     * Form Errors
     */
    public static String EmailFieldEmptyError = "Obrigatório preenchimento de email.";
    public static String EmailFieldMalFormedError = "Endereço de email mal formatado.";
    public static String PasswordFieldEmptyError = "Obrigatório preenchimento de senha.";
    public static String NameFieldEmptyError = "Obrigatório preenchimento de nome.";

    /**
     * ProgressBar Messages
     */
    public static String ProgressBarAnimationSignInUser = "Acessando sua conta...";
    public static String ProgressBarAnimationCreatingUser = "Criando usuário...";
    public static String ProgressBarAnimationChangingConfigs = "Alterando configurações...";

}
