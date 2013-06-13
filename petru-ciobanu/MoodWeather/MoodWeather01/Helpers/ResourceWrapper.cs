namespace MoodWeather
{
    /// <summary>
    ///     Wraps access to the strongly typed resource classes so that you can bind
    ///     control properties to resource strings in XAML
    /// </summary>
    public sealed class ResourceWrapper
    {
        private static readonly ApplicationStrings applicationStrings = new ApplicationStrings();
        private static readonly SecurityQuestions securityQuestions = new SecurityQuestions();
        private static readonly UserRoleManager userRoles = new UserRoleManager();

        /// <summary>
        ///     Gets the <see cref="ApplicationStrings" />.
        /// </summary>
        public ApplicationStrings ApplicationStrings
        {
            get { return applicationStrings; }
        }

        /// <summary>
        ///     Gets the <see cref="SecurityQuestions" />.
        /// </summary>
        public SecurityQuestions SecurityQuestions
        {
            get { return securityQuestions; }
        }


        public UserRoleManager UserRoleManager
        {
            get { return userRoles; }
        }
    }
}