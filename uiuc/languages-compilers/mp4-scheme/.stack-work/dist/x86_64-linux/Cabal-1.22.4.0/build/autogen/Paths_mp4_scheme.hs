module Paths_mp4_scheme (
    version,
    getBinDir, getLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
catchIO = Exception.catch

version :: Version
version = Version [0,1,0,0] []
bindir, libdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp4-scheme/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/bin"
libdir     = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp4-scheme/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/lib/x86_64-linux-ghc-7.10.2/mp4-scheme-0.1.0.0-KB161GPZ0heAeaJLt0YcKY"
datadir    = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp4-scheme/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/share/x86_64-linux-ghc-7.10.2/mp4-scheme-0.1.0.0"
libexecdir = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp4-scheme/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/libexec"
sysconfdir = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp4-scheme/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/etc"

getBinDir, getLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "mp4_scheme_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "mp4_scheme_libdir") (\_ -> return libdir)
getDataDir = catchIO (getEnv "mp4_scheme_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "mp4_scheme_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "mp4_scheme_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
