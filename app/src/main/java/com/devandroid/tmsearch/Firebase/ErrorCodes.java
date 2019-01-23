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
    public static String EmailFieldEmptyError = "Email required.";
    public static String EmailFieldMalFormedError = "Email bad format.";
    public static String PasswordFieldEmptyError = "Password required.";
    public static String NameFieldEmptyError = "Name required.";

    /**
     * ProgressBar Messages
     */
    public static String ProgressBarAnimationSignInUser = "Login in your account...";
    public static String ProgressBarAnimationCreatingUser = "Creating user...";
    public static String ProgressBarAnimationChangingConfigs = "Changing configuraations...";

}
